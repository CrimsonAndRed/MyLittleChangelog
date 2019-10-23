package my.little.changelog.service;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.ebean.Ebean;
import io.ebean.ExpressionList;
import io.ebean.FetchConfig;
import io.ebean.SqlRow;
import lombok.extern.log4j.Log4j2;
import my.little.changelog.error.CustomError;
import my.little.changelog.error.ErrorMessage;
import my.little.changelog.error.Errorable;
import my.little.changelog.global.OrikaMapper;
import my.little.changelog.model.auth.User;
import my.little.changelog.model.project.Changelog;
import my.little.changelog.model.project.Project;
import my.little.changelog.model.project.Route;
import my.little.changelog.model.project.Version;
import my.little.changelog.model.project.dto.*;
import my.little.changelog.validator.VersionValidator;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Versions-related service.
 */
@Log4j2
@Singleton
public class VersionService {

    @Inject
    private ProjectService projectService;

    @Inject
    private OrikaMapper beanMapper;

    @Inject
    private VersionValidator versionValidator;

    @Inject
    private ChangelogService changelogService;

    /**
     * Finds latest version's internal order in database by project id.
     * Has params:
     * - project id - id of project.
     */
    private static final String QUERY_MAX_VERSION_ID = "SELECT MAX(internal_order) AS max_order FROM project_version v WHERE v.project_id = :projectId";

    /**
     * Compares RouteDto in appearing order.
     */
    private static final Comparator<RouteDto> ROUTE_DTO_COMPARATOR = Comparator.comparing(r1 -> r1.getId(), Comparator.nullsLast(Comparator.naturalOrder()));

    /**
     * Compares ChangelogDto in appearing order.
     */
    private static final Comparator<ChangelogDto> CHANGELOG_DTO_COMPARATOR = Comparator.comparing(r1 -> r1.getId(), Comparator.nullsLast(Comparator.naturalOrder()));

    /**
     * Find one version by id.
     * Fetches changelogs and routes.
     * @param id identifier of version.
     * @return version or null.
     */
    @Nullable
    public Version getFullVersionById(Long id) {
        Version result = Ebean.find(Version.class)
                .fetch("changelogs", new FetchConfig().query())
                .fetch("project")
                .fetch("project.routes", new FetchConfig().query())
                .fetch("changelogs.route", new FetchConfig().query())
                .fetch("project.createUser")
                .where()
                .eq("id", id)
                .findOne();
        return result;
    }

    /**
     * Creates DTO from model.
     * Too complicated to do it with orika.
     * @param version model.
     * @return dto of version.
     */
    @Nullable
    public FullVersionDto createVersionDtoByVersion(@Nullable Version version) {
        if (version == null) {
            return null;
        }
        FullVersionDto result = new FullVersionDto();
        result.setId(version.getId());
        result.setNum(version.getNum());
        result.setDescription(version.getDescription());
        result.setInternalOrder(version.getInternalOrder());
        result.setV(version.getV());


        MinimalisticProjectDto minimalisticProjectDto = beanMapper.map(version.getProject(), MinimalisticProjectDto.class);
        result.setProject(minimalisticProjectDto);

        // Grouping changelogs by routes
        List<Changelog> changelogs = version.getChangelogs();

        // Map<Route id, route dto>
        Map<Long, RouteDto> routeDtoById = version.getProject().getRoutes()
                .stream()
                .map(item -> beanMapper.map(item, RouteDto.class))
                .collect(
                        Collectors.toMap(
                                item -> item.getId(),
                                item -> item
                        )
                );
        for (Changelog changelog : changelogs) {
            Route currentRoute = changelog.getRoute();
            Long routeId = currentRoute.getId();
            RouteDto route = routeDtoById.get(routeId);

            ChangelogDto changelogDto = beanMapper.map(changelog, ChangelogDto.class);
            route.getChangelogs().add(changelogDto);
        }

        List<RouteDto> routes = Lists.newArrayList(routeDtoById.values());
        routes.sort(ROUTE_DTO_COMPARATOR);
        for (RouteDto route : routes) {
            route.getChangelogs().sort(CHANGELOG_DTO_COMPARATOR);
        }

        result.setRoutes(routes);
        return result;
    }

    /**
     * Creates version from user inputs.
     *
     * @param versionDto user's input.
     * @param user       user, that send request.
     * @return saved entity of {@link Version}.
     */
    public Errorable<Version> createVersion(CreateVersionDto versionDto, User user) {

        Long projectId = versionDto.getProjectId();
        Project project = Ebean.find(Project.class)
                .fetch("createUser")
                .where()
                .eq("id", projectId)
                .findOne();

        // Project has beed deleted?
        if (project == null) {
            return new Errorable<>(null, ErrorMessage.genericError());
        }

        if (!Objects.equals(project.getCreateUser().getId(), user.getId())) {
            return new Errorable<>(null, ErrorMessage.userNotAllowed());
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

        return new Errorable<>(version);
    }

    /**
     * Moves version up or down in all project's version list.
     * This manipulates {@link Version#internalOrder} field.
     *
     * @param newOrder     new order number for current version.
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

    /**
     * Deletes version and moves all other versions interanl order.
     * @param versionId version id.
     * @param user user, who requested deletion.
     * @return error text or nothing.
     */
    public Errorable<Void> deleteVersion(Long versionId, User user) {
        if (versionId == null) {
            return new Errorable<>(null, new CustomError("Missed version"));
        }

        Version version = Ebean.find(Version.class).setId(versionId).findOne();
        if (version == null) {
            return new Errorable<>(null, new CustomError("Missed version"));
        }

        Long internalOrder = version.getInternalOrder();

        if (user == null) {
            return new Errorable<>(null, new CustomError("Missed user"));
        }

        Project project = Ebean.find(Project.class)
                .where()
                .eq("versions.id", version.getId())
                .findOne();

        if (project == null) {
            return new Errorable<>(null, new CustomError("Missed project"));
        }

        if (!Objects.equals(project.getCreateUser().getId(), user.getId())) {
            return new Errorable<>(null, "User is not an owned of the project");
        }
        int rowsCount = Ebean.delete(Version.class, version.getId());

        if (rowsCount == 0) {
            return new Errorable<>(null, new CustomError("Missed version identifier"));
        }
        List<Version> toUpdate = Ebean.find(Version.class)
                .where()
                .eq("project.id", project.getId())
                .gt("internalOrder", internalOrder)
                .findList();
        for (Version vers : toUpdate) {
            vers.setInternalOrder(vers.getInternalOrder() - 1);
            Ebean.update(vers);
        }
        return new Errorable<>();
    }

    /**
     * Validates version and saves it.
     * @param version version model.
     * @param user user, who requested save.
     * @return errorable of version.
     */
    public Errorable<Version> saveVersion(Version version, User user) {

        List<CustomError> errors = versionValidator.validate(version);

        if (!errors.isEmpty()) {
            return new Errorable<>(version, errors);
        }

        List<Changelog> newChangelogs = version.getChangelogs()
                .stream()
                .filter(item -> item.getId() == null)
                .collect(Collectors.toList());
        List<Long> newIds = changelogService.nextIds((long)newChangelogs.size());

        for (int i = 0; i < newChangelogs.size(); i++) {
            Changelog changelog = newChangelogs.get(i);
            Long newId = newIds.get(i);

            changelog.setId(newId);
            if (changelog.getVid() == null) {
                changelog.setVid(newId);
            }
        }
        Ebean.update(version);
        return new Errorable<>(version);
    }
}