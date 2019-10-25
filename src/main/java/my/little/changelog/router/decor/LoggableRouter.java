package my.little.changelog.router.decor;

import lombok.extern.log4j.Log4j2;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * Router, that logs incoming messages.
 */
@Log4j2
public class LoggableRouter extends RouterDecorator {

    /**
     * Decorators constructor.
     * @param decorated object to be decorated
     */
    public LoggableRouter(Route decorated) {
        super(decorated);
    }

    /**
     * {@inheritDoc}.
     * Adds message to Logger.
     */
    @Override
    public Object handle(Request req, Response res) throws Exception {
        log.info("Handling request to path {} \"{}\" by user \"{}\"", req.requestMethod(), req.uri(), req.ip());
        Object result = super.handle(req, res);
        log.info("Request to path {} \"{}\" by user \"{}\" has finished", req.requestMethod(), req.uri(), req.ip());
        return result;
    }
}
