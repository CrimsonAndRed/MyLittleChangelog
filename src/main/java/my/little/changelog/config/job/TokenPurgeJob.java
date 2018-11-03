package my.little.changelog.config.job;

import com.google.common.base.Throwables;
import com.google.common.util.concurrent.AbstractScheduledService;
import io.ebean.Ebean;
import io.ebean.Transaction;
import lombok.extern.log4j.Log4j2;
import my.little.changelog.model.auth.UserToken;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

/**
 * Every hour we soft delete all tokens, that have been created more than 24 hour ago.
 */
@Log4j2
public class TokenPurgeJob extends AbstractScheduledService {

    /**
     * Query to soft delete tokens, that have been created more than 24 hour ago.
     * Params:
     * - yesterday - date to compare. Lower dates than this date will be soft deleted.
     */
    private static final String PURGE_TOKENS = "UPDATE user_token SET is_deleted = true, end_date = now() WHERE create_date < :dateBound AND is_deleted = false";

    @Override
    protected void runOneIteration() throws Exception {
        try (Transaction t = Ebean.beginTransaction()) {

            int rowsDeleted = Ebean.createUpdate(UserToken.class, PURGE_TOKENS)
                    .setParameter("dateBound", LocalDateTime.now().minus(1, ChronoUnit.DAYS))
                    .execute();
            log.info("Executed purge tokens job. Deleted {} tokens", rowsDeleted);
            t.commit();
        } catch (Exception e) {
            log.error("Could not purge tokens from database");
            log.error(Throwables.getStackTraceAsString(e));
        }
    }

    @Override
    protected Scheduler scheduler() {
        return Scheduler.newFixedRateSchedule(0, 1, TimeUnit.HOURS);
    }
}
