package my.little.changelog.router.decor;

import com.google.common.base.Strings;
import io.ebean.Ebean;
import lombok.extern.log4j.Log4j2;
import my.little.changelog.config.Configurator;
import my.little.changelog.model.auth.UserToken;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

import java.util.UUID;

/**
 * Router that checks user to be log on.
 * Throws 401 in case wrong user.
 */
@Log4j2
public class AuthRouter extends RouterDecorator {

    /**
     * Unauthorized http code.
     */
    private static final int UNAUTHORIZED = 401;

    /**
     * Decorators constructor.
     *
     * @param decorated object to be decorated.
     */
    public AuthRouter(Route decorated) {
        super(decorated);
    }

    /**
     * {@inheritDoc}.
     * Checks if user is authorized to perform actions.
     * If not - return 401.
     */
    @Override
    public Object handle(Request req, Response res) throws Exception {

        String token = req.headers(Configurator.TOKEN_HEADER);
        if (Strings.isNullOrEmpty(token)) {
            log.warn("Request did not have token \"{}\". Request was aborted with 401 code.", Configurator.TOKEN_HEADER);
            Spark.halt(UNAUTHORIZED);
            return null;
        }

        UserToken userToken = Ebean.find(UserToken.class)
                .select("id, createDate")
                .fetch("user", "id, name, deleted")
                .where()
                .eq("token", UUID.fromString(token))
                .eq("deleted", false)
                .findOne();
        if (userToken == null) {
            log.warn("Token \"{}\" was not found. Request was aborted with 401 code.", token);
            Spark.halt(UNAUTHORIZED);
            return null;
        } else if (userToken.getUser().getDeleted()) {
            log.warn("Token \"{}\" was found, but user \"{}\" is deleted. Request was aborted with 401 code.", token, userToken.getUser().getName());
            Spark.halt(UNAUTHORIZED);
            return null;
        } else {
            log.info("User \"{}\" found for current token \"{}\"", userToken.getUser().getName(), token);
        }

        return super.handle(req, res);
    }
}
