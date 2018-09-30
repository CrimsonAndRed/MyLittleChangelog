package my.little.changelog.router.decor;


import lombok.extern.log4j.Log4j2;
import spark.Request;
import spark.Response;
import spark.Route;

import java.time.Duration;

/**
 * Logger, that measures time to call Controller's method.
 */
@Log4j2
public class MeasureRouter extends RouterDecorator {

    /**
     * Index of numbers in some kind of tricky {@link Duration#toString()}.
     */
    private static final int INDEX_OF_DURATION_NUMBERS = 2;

    /**
     * Decorators constructor.
     * @param decorated object to be decorated
     */
    public MeasureRouter(Route decorated) {
        super(decorated);
    }

    /**
     * {@inheritDoc}.
     * Logs time consumed on controller operation in pretty format.
     */
    @Override
    public Object handle(Request req, Response res) throws Exception {
        long startTime = System.currentTimeMillis();
        res.type("application/json");

        Object result = super.handle(req, res);
        // Some kind of hack?
        String durationLog = Duration
                .ofMillis(System.currentTimeMillis() - startTime)
                .toString()
                .substring(INDEX_OF_DURATION_NUMBERS)
                .replaceAll("(\\d[HMS])(?!$)", "$1 ")
                .toLowerCase();
        log.debug("Request to path \"{}\" by user \"{}\" has ended within {}", req.uri(), req.ip(), durationLog);
        return result;
    }
}
