package my.little.changelog.router.decor;

import com.fasterxml.jackson.databind.ObjectMapper;
import my.little.changelog.global.GuiceInjector;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * Router, that returns Json.
 */
public class JsonRouter extends RouterDecorator {

    /**
     * Decorators constructor.
     * @param decorated object to be decorated.
     */
    public JsonRouter(Route decorated) {
        super(decorated);
    }

    /**
     * {@inheritDoc}.
     * Adds "application/json" response header and maps result of controllers method to Json.
     */
    @Override
    public Object handle(Request req, Response res) throws Exception {
        Object result = super.handle(req, res);
        res.type("application/json");
        ObjectMapper mapper = GuiceInjector.getInjector().getInstance(ObjectMapper.class);
        return mapper.writeValueAsString(result);
    }
}
