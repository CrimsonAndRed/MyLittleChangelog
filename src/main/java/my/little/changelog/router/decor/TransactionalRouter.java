package my.little.changelog.router.decor;

import io.ebean.Ebean;
import io.ebean.Transaction;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * Router that decorates method call with Ebean's transaction.
 */
public class TransactionalRouter extends RouterDecorator {

    /**
     * Decorators constructor.
     * @param decorated object to be decorated.
     */
    public TransactionalRouter(Route decorated) {
        super(decorated);
    }

    @Override
    public Object handle(Request req, Response res) throws Exception {
        Object result;
        try (Transaction t = Ebean.beginTransaction()) {
            result = super.handle(req, res);
            t.commit();
        }
        return result;
    }
}
