package my.little.changelog.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.ebean.*;
import lombok.extern.log4j.Log4j2;
import my.little.changelog.error.ErrorMessage;
import my.little.changelog.error.Errorable;
import my.little.changelog.model.auth.User;
import my.little.changelog.model.project.Project;
import my.little.changelog.model.project.Version;
import my.little.changelog.model.project.dto.CreateVersionDto;
import my.little.changelog.model.project.dto.MinimalisticVersionDto;

import java.util.List;
import java.util.Objects;

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
     * @param versionDto user's input.
     * @param user user, that send request.
     * @return saved entity of {@link Version}.
     */
    public Errorable createVersion(CreateVersionDto versionDto, User user) {

        Long projectId = versionDto.getProjectId();
        Project project = Ebean.find(Project.class)
                .fetch("createUser")
                .where()
                .eq("id", projectId)
                .findOne();

        // Project has beed deleted?
        if (project == null) {
            return new Errorable(null, ErrorMessage.genericError());
        }

        if (!Objects.equals(project.getCreateUser().getId(), user.getId())) {
            return new Errorable(null, ErrorMessage.userNotAllowed());
        }
        // TODO This is error-prone, because not synchronised
        SqlRow max = Ebean.createSqlQuery(QUERY_MAX_VERSION_ID).setParameter("projectId", projectId).findOne();

        Long currentOrder = 0L;
        if (max != null) {
            Long maxOrder = max.getLong("max_order");
            if (maxOrder != null) {
                currentOrder = maxOrder + 1;
            }
        }

        Version version = new Version();
        version.setInternalOrder(currentOrder);
        version.setProject(project);
        version.setNum(versionDto.getNum());
        version.save();

        return new Errorable(version);
    }

    /**
     * Moves version up or down in all project's version list.
     * This manipulates {@link Version#internalOrder} field.
     * @param newOrder new order number for current version.
     * @param versionModel current version.
     * @return saved entity of {@link Version}
     */
    public Version moveVersion(Long newOrder, Version versionModel) {

        Long id = versionModel.getId();
        Long currentOrder = versionModel.getInternalOrder();

        Version currentVersion = Ebean.find(Version.class)
                .fetch("project", "id")
                .where()
                .idEq(id)
                .findOne();

        if (currentVersion == null) {
            throw new IllegalArgumentException("Could not find version by id " + id);
        }

        ExpressionList<Version> expression = Ebean.find(Version.class)
                .where()
                .eq("project.id", currentVersion.getProject().getId());

        if (newOrder > versionModel.getInternalOrder()) {
            List<Version> toUpdate = expression
                    .le("internalOrder", newOrder)
                    .gt("internalOrder", currentOrder)
                    .findList();


            versionModel.setInternalOrder(newOrder);
            Ebean.update(versionModel);

            for (Version version : toUpdate) {
                version.setInternalOrder(version.getInternalOrder() - 1);
                Ebean.save(version);
            }
        } else {
            List<Version> toUpdate = expression
                    .lt("internalOrder", currentOrder)
                    .ge("internalOrder", newOrder)
                    .findList();
            versionModel.setInternalOrder(newOrder);
            Ebean.update(versionModel);

            for (Version version : toUpdate) {
                version.setInternalOrder(version.getInternalOrder() + 1);
                Ebean.save(version);
            }
        }


        return versionModel;
    }
}