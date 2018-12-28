package my.little.changelog.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.ebean.Ebean;
import lombok.extern.log4j.Log4j2;
import my.little.changelog.error.Errorable;
import my.little.changelog.model.auth.User;
import my.little.changelog.model.auth.UserToken;
import my.little.changelog.model.auth.dto.LoginDto;
import my.little.changelog.model.auth.dto.LoginResponseDto;
import my.little.changelog.annotation.NotNull;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Authentication things service.
 */
@Singleton
@Log4j2
public class AuthService {

    /**
     * Message to display on wrong credentials.
     */
    private static final String REJECT_USER_MESSAGE = "Login or password is wrong";

    /**
     * Query to lock tokens database table.
     * To exclude any kind of collision between tokens.
     */
    private static final String LOCK_TOKENS_TABLE = "LOCK TABLE user_token IN SHARE ROW EXCLUSIVE MODE";

    @Inject
    private CryptoService cryptoService;

    /**
     * Trying to verify user's credentials.
     *
     * @param loginDto user's credentials.
     * @return errorable with {@link LoginResponseDto} inside (or null in error case).
     */
    public Errorable login(LoginDto loginDto) {
        if (loginDto == null || loginDto.getLogin() == null || loginDto.getPassword() == null) {
            return new Errorable(null, REJECT_USER_MESSAGE);
        }
        String login = loginDto.getLogin();

        User user = Ebean.find(User.class)
                .select("id, name, password, salt")
                .where()
                .eq("name", login)
                .eq("deleted", false)
                .findOne();
        if (user == null) {
            log.debug("Could not find user by login \"{}\"", login);
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
            log.debug("User's \"{}\" password does not match database one", login);
            return new Errorable(null, "Login or password is wrong.");
        }

        log.debug("Successfully logged in user \"{}\"", login);

        UserToken token = new UserToken();
        token.setUser(user);
        token.setDeleted(false);

        // Doing it in case uuid collisions.
        // Anyway will be unlocked because this method call is wrapped in transaction BEGIN - COMMIT - ROLLBACK.
        Ebean.createSqlUpdate(LOCK_TOKENS_TABLE).execute();
        UUID randomUuid;
        // Defending myself from collisions, lets be secure at least in this particular little feature
        // This could be infinite loop
        // in case of REALLY massive amount of users at the same time.
        // TODO maybe while-true is not that good??
        while(true) {
            randomUuid = UUID.randomUUID();
            log.debug("Trying to find \"{}\" token in database", randomUuid);
            int count = Ebean.find(UserToken.class)
                    .where()
                    .eq("token", randomUuid)
                    .eq("deleted", false)
                    .findCount();
            if (count == 0) {
                log.debug("Token \"{}\" is unique, taking it", randomUuid);
                token.setToken(randomUuid);
                break;
            }
            log.debug("Token \"{}\" is not unique, retrying", randomUuid);
        }
        token.save();

        // Returning new token and username
        LoginResponseDto response = new LoginResponseDto(randomUuid, user.getName());
        return new Errorable(response);
    }

    /**
     * Logging out user.
     * In any case.
     * @param token user's token.
     */
    public void logout(@NotNull String token) {
        List<UserToken> tokens = Ebean.find(UserToken.class)
                .where()
                .eq("token", UUID.fromString(token))
                .eq("deleted", false)
                .findList();

        log.debug("Trying to delete token \"{}\"", token);
        log.debug("Found {} tokens", tokens.size());

        for (UserToken userToken : tokens) {
            userToken.setEndDate(Instant.now());
            userToken.setDeleted(true);
            userToken.save();
        }
    }
}
