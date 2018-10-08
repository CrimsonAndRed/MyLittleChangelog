package my.little.changelog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Throwables;
import com.google.inject.Inject;
import lombok.extern.log4j.Log4j2;
import my.little.changelog.error.MyInternalError;
import spark.Request;
import spark.Response;

/**
 * Controller for any administration tasks.
 */
@Log4j2
public class AdministrationController {

    /**
     * Internal error http code.
     */
    private static final int INTERNAL_ERROR_CODE = 500;

    @Inject
    private ObjectMapper mapper;

    /**
     * Handling errors.
     * Could be a little performance sink - getting full stacktrace twice.
     * But it might be easier to investigate problem when stacktrace is available.
     * May be changed in future.
     * @param re    RuntimeException, any kind inheritor.
     * @param req   Spark request
     * @param res   Spark response
     */
    public void handleInternalError(RuntimeException re, Request req, Response res) {
        log.error(Throwables.getStackTraceAsString(re));
        res.status(INTERNAL_ERROR_CODE);
        try {
            res.body(mapper.writeValueAsString(new MyInternalError(re)));
        } catch (Exception e) {
            log.error("Could not serialize error. Returning empty body.");
        }
    }
}
