package my.little.changelog.router;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import lombok.extern.log4j.Log4j2;
import my.little.changelog.controller.AuthController;
import my.little.changelog.global.GlobalParams;
import my.little.changelog.router.decor.*;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

import java.util.function.BiFunction;

/**
 * Main class for web routes configuration.
 */
@Log4j2
@Singleton
public class Router {

    /** Parameter for application port ot use. */
    public static final String PORT_PARAM = "web.port";

    /** Default value for application port. */
    public static final String DEFAULT_PORT_PARAM = "9000";

    @Inject
    private ObjectMapper mapper;

    @Inject
    private GlobalParams globalParams;

    @Inject
    private AuthController authController;

    @Inject
    private Injector injector;

    /** Utility class constructor. */
    public Router() {
    }

    /**
     * CORS handling for everyone.
     * Copy pasta from http://sparkjava.com/tutorials/cors.
     */
    private static void enableCORS() {

        Spark.options("/*", (request, response) -> {

            String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
            }

            String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
            }

            return "OK";
        });

        Spark.before((request, response) -> {
            response.header("Access-Control-Allow-Origin", "*");
            response.header("Access-Control-Request-Method", "POST, GET, OPTIONS, DELETE, PUT");
            response.header("Access-Control-Allow-Headers", "*");
        });
    }

    /**
     * Configures routes to be applied to Spark.
     */
    public void initRoutes() {

        int port = Integer.parseInt(globalParams.get(PORT_PARAM));
        Spark.port(port);
        enableCORS();

        Spark.post("/login",
            Router.Builder.create(authController::login)
                .json()
                .measure()
                .transaction()
                .log()
                .build());

        Spark.post("/logout", Router.Builder.create(authController::logout)
                .measure()
                .transaction()
                .log()
                .build());

    }

    /**
     * Handy RouterDecorator wrapper.
     * Decorators will apply in reverse order of method call.
     */
    private static class Builder {

        /**
         * Decorator holder.
         */
        private Route decorator;

        /**
         * Private constructor for prettier.
         * @param controllerFunc controller's function to be wrapped with functionality.
         */
        private Builder(BiFunction<Request, Response, Object> controllerFunc){
            decorator = new BasicRouter(controllerFunc);
        }

        /**
         * Instead of public constructor.
         * Creates BasicRouter without additional functionality.
         * @param controllerFunc controller's function to be wrapped with functionality.
         * @return Builder instance.
         */
        public static Builder create(BiFunction<Request, Response, Object> controllerFunc) {
            return new Builder(controllerFunc);
        }

        /**
         * Wraps method with Json handling functionality.
         * @return Builder
         */
        public Builder json() {
            this.decorator = new JsonRouter(decorator);
            return this;
        }

        /**
         * Wraps method with transaction handling functionality.
         * @return Builder
         */
        public Builder transaction() {
            this.decorator = new TransactionalRouter(decorator);
            return this;
        }

        /**
         * Wraps method with Json time measuring functionality.
         * @return Builder
         */
        public Builder measure() {
            this.decorator = new MeasureRouter(decorator);
            return this;
        }

        /**
         * Wraps method with authentication-check functionality.
         * @return Builder
         */
        public Builder auth() {
            this.decorator = new AuthRouter(decorator);
            return this;
        }

        /**
         * Wraps method with logging functionality.
         * @return Builder
         */
        public Builder log() {
            this.decorator = new LoggableRouter(decorator);
            return this;
        }

        /**
         * Builder endpoint.
         * @return Wrapped method call.
         */
        public Route build() {
            return decorator;
        }

        /**
         * Default builder, that wraps method call with default functionality (log -> auth -> transaction -> measure -> json -> method call).
         * @param controllerFunc function to be wrapped.
         * @return Wrapped method call.
         */
        public static Route createDefault(BiFunction<Request, Response, Object> controllerFunc) {
            return new Builder(controllerFunc).json().measure().transaction().auth().log().build();
        }

    }
}
