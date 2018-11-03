package my.little.changelog.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.ebean.Ebean;
import io.ebean.Transaction;
import my.little.changelog.error.Errorable;
import my.little.changelog.model.auth.User;
import my.little.changelog.model.auth.UserToken;
import my.little.changelog.model.auth.dto.LoginDto;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Authentication things service.
 */
@Singleton
public class AuthService {

    /**
     * Message to display on wrong credentials.
     */
    private static final String REJECT_USER_MESSAGE = "Login or password is wrong";

    /**
     * Query to lock tokens database table.
     * To exclude any kind of collision between tokens.
     */
    private static final String LOCK_TOKENS_TABLE = "LOCK TABLE user_token IN SHARE MODE";

    @Inject
    private CryptoService cryptoService;

    public Errorable login(LoginDto loginDto) {
        if (loginDto == null || loginDto.getLogin() == null || loginDto.getPassword() == null) {
            return new Errorable(null, REJECT_USER_MESSAGE);
        }

        User user = Ebean.find(User.class)
                .select("id, name, password, salt")
                .where()
                .eq("name", loginDto.getLogin())
                .eq("deleted", false)
                .findOne();
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
        Ebean.createSqlUpdate(LOCK_TOKENS_TABLE).execute();
        UUID randomUuid;
        // Defending myself from collisions, lets be secure at least in this particular little feature
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

    public void logout(String token) {
        List<UserToken> tokens = Ebean.find(UserToken.class)
                .where()
                .eq("token", UUID.fromString(token))
                .eq("deleted", false)
                .findList();

        Transaction transaction = Ebean.currentTransaction();
        transaction.setBatchMode(true);
        transaction.setBatchSize(tokens.size());

        for (UserToken userToken : tokens) {
            userToken.setEndDate(LocalDateTime.now());
            userToken.setDeleted(true);
            userToken.save();
        }
    }
}
