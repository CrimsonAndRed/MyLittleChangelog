package my.little.changelog.router.decor;

import spark.Request;
import spark.Response;
import spark.Route;

import java.util.function.BiFunction;

/**
 * Router that evaluates Controller's function.
 * The only endpoint of {@link Route} to be decorated.
 */
public class BasicRouter implements Route {

    /**
     * Function to be applied.
     */
    protected final BiFunction<Request, Response, Object> controllerFunc;

    /**
     * Endpoint decorator highly coupled with controllers method call.
     * @param func controllers method.
     */
    public BasicRouter(BiFunction<Request, Response, Object> func) {
        this.controllerFunc = func;
    }

    @Override
    public Object handle(Request req, Response res) throws Exception {
        return controllerFunc.apply(req, res);
    }
}
