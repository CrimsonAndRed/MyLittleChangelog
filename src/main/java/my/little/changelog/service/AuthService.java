package my.little.changelog.service;

import com.google.inject.Singleton;
import io.ebean.Ebean;
import my.little.changelog.model.auth.User;
import my.little.changelog.model.auth.UserToken;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

/**
 * Authentication things service.
 */
@Singleton
public class AuthService {

    /**
     * Check for any token to be found in database.
     * @param token token value to be checked.
     * @return Token model or null
     */
    @Nullable
    public UserToken checkUserByToken(@Nonnull String token) {
        UserToken userToken = Ebean.find(UserToken.class)
                .select("id, createDate")
                .fetch("user", "id, name, deleted")
                .where()
                .eq("token", UUID.fromString(token))
                .eq("deleted", false)
                .findOne();

        return userToken;
    }

    /**
     * Checks that user exists and is actual by login.
     * @param login login
     * @return User model or null.
     */
    @Nullable
    public User checkUserLogin(@Nullable String login) {

        User user = Ebean.find(User.class)
                .select("id, name, password, salt")
                .where()
                .eq("name", login)
                .eq("deleted", false)
                .findOne();

        return user;

    }
}
