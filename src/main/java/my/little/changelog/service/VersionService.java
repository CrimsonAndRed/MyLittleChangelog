package my.little.changelog.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.ebean.Ebean;
import io.ebean.SqlRow;
import lombok.extern.log4j.Log4j2;
import my.little.changelog.error.ErrorMessage;
import my.little.changelog.error.Errorable;
import my.little.changelog.model.auth.User;
import my.little.changelog.model.project.Version;

/**
 * Versions-related service.
 */
@Log4j2
@Singleton
public class VersionService {

    @Inject
    private ProjectService projectService;

    /**
     * Finds latest version's internal order in database by project id.
     * Has params:
     * - project id - id of project.
     */
    private static final String QUERY_MAX_VERSION_ID = "SELECT MAX(internal_order) AS max_order FROM project_version v WHERE v.project_id = :projectId";

    /**
     * Creates version from user inputs.
     * @param version user input.
     * @param projectId project's identifier.
     * @return saved entity of {@link Version}.
     */
    public Errorable createVersion(Version version, User user, Long projectId) {

        boolean isOwner = projectService.checkUserIsOwner(projectId, user.getId());
        if (!isOwner) {
            return new Errorable(null, ErrorMessage.userNotAllowed());
        }

        Ebean.find(Version.class)
                .fetch("project", "id")
                .where()
                .eq("project.id", projectId)
                .findOne();
        SqlRow max = Ebean.createSqlQuery(QUERY_MAX_VERSION_ID).setParameter("projectId", projectId).findOne();

        Long currentOrder = 0L;
        if (max != null) {
            Long maxOrder = max.getLong("max_order");
            if (maxOrder != null) {
                currentOrder = maxOrder + 1;
            }
        }
        version.setInternalOrder(currentOrder);
        version.save();

        return new Errorable(version);
    }
}
