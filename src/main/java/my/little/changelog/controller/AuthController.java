package my.little.changelog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.ebean.Ebean;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import my.little.changelog.config.Configurator;
import my.little.changelog.error.Errorable;
import my.little.changelog.model.auth.User;
import my.little.changelog.model.auth.UserToken;
import my.little.changelog.model.auth.dto.LoginDto;
import my.little.changelog.service.AuthService;
import my.little.changelog.service.CryptoService;
import spark.Request;
import spark.Response;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Controls authentication process and other regular account-based problems.
 */
@Log4j2
@Singleton
public class AuthController {

    /**
     * Message to display on wrong credentials.
     */
    private static final String REJECT_USER_MESSAGE = "Login or password is wrong";

    @Inject
    private ObjectMapper mapper;

    @Inject
    private CryptoService cryptoService;

    @Inject
    private AuthService authService;

    /**
     * Log user in.
     * Contains user login and password.
     * @param req default Spark request.
     * @param res default Spark response.
     * @return User token or error.
     */
    @SneakyThrows
    public Errorable login(Request req, Response res) {

        String body = req.body();
        LoginDto loginDto = mapper.readValue(body, LoginDto.class);

        if (loginDto == null || loginDto.getLogin() == null || loginDto.getPassword() == null) {
            return new Errorable(null, REJECT_USER_MESSAGE);
        }

        User user = authService.checkUserLogin(loginDto.getLogin());
        if (user == null) {
            return new Errorable(null, "Login or password is wrong.");
        }

        byte[] password = loginDto.getPassword().getBytes();
        byte[] salt = user.getSalt();
        if (salt != null) {
            int length = password.length + salt.length;
            byte[] result = new byte[length];
            System.arraycopy(password, 0, result, 0, password.length);
            System.arraycopy(salt, 0, result, password.length, salt.length);
            password = result;
        }
        byte[] saltedHash = cryptoService.hashPassword(password);

        if (!Arrays.equals(user.getPassword(), saltedHash)) {
            return new Errorable(null, "Login or password is wrong.");
        }

        UserToken token = new UserToken();
        token.setUser(user);
        token.setDeleted(false);
        token.setCreateDate(LocalDateTime.now());

        // Doing it in case uuid collisions.
        // Anyway will be unlocked because this method call is wrapped in transaction BEGIN - COMMIT - ROLLBACK.
        String lockTable = "LOCK TABLE user_token IN SHARE MODE";
        Ebean.createSqlUpdate(lockTable).execute();
        UUID randomUuid;
        // This could be infinite loop
        // in case of REALLY massive amount of users at the same time.
        while(true) {
            randomUuid = UUID.randomUUID();
            int count = Ebean.find(UserToken.class)
                    .where()
                    .eq("token", randomUuid)
                    .eq("deleted", false)
                    .findCount();
            if (count == 0) {
                token.setToken(randomUuid);
                break;
            }
        }
        token.save();
        return new Errorable(randomUuid.toString());
    }

    /**
     *  Drops user session.
     * @param req default Spark request.
     * @param res default Spark response.
     * @return Success string.
     */
    public Object logout(Request req, Response res) {

        String token = req.headers(Configurator.TOKEN_HEADER);

        List<UserToken> tokens = Ebean.find(UserToken.class)
                .where()
                .eq("token", UUID.fromString(token))
                .eq("deleted", false)
                .findList();

        for (UserToken userToken : tokens) {
            userToken.setEndDate(LocalDateTime.now());
            userToken.setDeleted(true);
            userToken.save();
        }
        return "Success";
    }
}
