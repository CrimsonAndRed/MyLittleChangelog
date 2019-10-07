package my.little.changelog.router.decor;

import lombok.extern.log4j.Log4j2;
import spark.Filter;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * Decorator for Spark's {@link Route} to run custom function on each route.
 * This is sort of overkill due to {@link spark.Spark#before(Filter...)}, {@link spark.Spark#after(Filter...)} etc.
 * But i just wanted to try my own way.
 */
@Log4j2
public abstract class RouterDecorator implements Route {
    /**
     * Decorator pattern implementation.
     */
    protected final Route decorated;

    /**
     * Decorators constructor.
     * @param decorated object to be decorated
     */
    public RouterDecorator(Route decorated) {
        this.decorated = decorated;
    }

    @Override
    public Object handle(Request req, Response res) throws Exception {
        return decorated.handle(req, res);
    }
}
