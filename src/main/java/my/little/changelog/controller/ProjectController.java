package my.little.changelog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Throwables;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.ebean.Ebean;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import my.little.changelog.config.Configurator;
import my.little.changelog.error.Errorable;
import my.little.changelog.global.OrikaMapper;
import my.little.changelog.model.auth.UserToken;
import my.little.changelog.model.project.Project;
import my.little.changelog.model.project.dto.FullProjectDto;
import my.little.changelog.model.project.dto.MinimalisticProjectDto;
import my.little.changelog.model.project.dto.MinimalisticVersionDto;
import my.little.changelog.service.ProjectService;
import spark.Request;
import spark.Response;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Controller for all project-related features.
 */
@Log4j2
@Singleton
public class ProjectController {

    @Inject
    private ProjectService projectService;

    @Inject
    private OrikaMapper beanMapper;

    @Inject
    private ObjectMapper mapper;

    /**
     * Find all projects in system.
     * @param req default Spark request.
     * @param res default Spark response.
     * @return list of all projects {@link MinimalisticProjectDto}.
     */
    public List<MinimalisticProjectDto> getAllProjects(Request req, Response res) {
        List<Project> allProjects = projectService.getMinimalisticAll();
        List<MinimalisticProjectDto> result = beanMapper.mapAsList(allProjects, MinimalisticProjectDto.class);
        result.sort(MinimalisticProjectDto.COMPARATOR_BY_ID);
        return result;
    }

    /**
     * Find all projects, which owner is current user.
     * @param req default Spark request.
     * @param res default Spark response.
     * @return list of projects.
     */
    public List<MinimalisticProjectDto> getUsersProjects(Request req, Response res) {
        String token = req.headers(Configurator.TOKEN_HEADER);
        UserToken userToken = Ebean.find(UserToken.class)
                .select("id, createDate")
                .fetch("user", "id, name, deleted")
                .where()
                .eq("token", UUID.fromString(token))
                .eq("deleted", false)
                .findOne();

        if (userToken == null) {
            return Collections.emptyList();
        }
        List<Project> allProjects = projectService.getMinimalisticByUser(userToken.getUser());
        List<MinimalisticProjectDto> result = beanMapper.mapAsList(allProjects, MinimalisticProjectDto.class);
        result.sort(MinimalisticProjectDto.COMPARATOR_BY_ID);
        return result;
    }

    /**
     * Find project by id.
     * Returns minimalistic information of project (only displayable) or error in case malformed parameter.
     * Has request parameters:
     * - id - identifier of project.
     * @param req default Spark request.
     * @param res default Spark response.
     * @return Errorable with one project data {@link MinimalisticProjectDto}.
     */
    public Errorable getMinimalProjectById(Request req, Response res) {
        String paramId = req.params("id");
        Long id;
        if (paramId == null) {
            return new Errorable(null, "Request error: could not read id");
        } else {
            try {
                id = Long.valueOf(paramId);
            } catch (NumberFormatException e) {
                log.error(Throwables.getStackTraceAsString(e));
                return new Errorable(null, "Request error: could not parse number from " + paramId);
            }
        }
        Project project = projectService.getMinimalisticById(id);
        MinimalisticProjectDto result = beanMapper.map(project, MinimalisticProjectDto.class);
        return new Errorable(result);
    }

    /**
     * Find project by id.
     * Returns full information of project (model {@link Project} can be saved based on this information) or error in case malformed parameter.
     * Has request parameters:
     * - id - identifier of project.
     * @param req default Spark request.
     * @param res default Spark response.
     * @return Errorable with one project data {@link my.little.changelog.model.project.dto.FullProjectDto}.
     */
    public Errorable getFullProjectById(Request req, Response res) {
        String paramId = req.params("id");
        Long id;
        if (paramId == null) {
            return new Errorable(null, "Request error: could not read id");
        } else {
            try {
                id = Long.valueOf(paramId);
            } catch (NumberFormatException e) {
                log.error(Throwables.getStackTraceAsString(e));
                return new Errorable(null, "Request error: could not parse number from " + paramId);
            }
        }
        Project project = projectService.getMinimalisticById(id);
        FullProjectDto result = beanMapper.map(project, FullProjectDto.class);
        if (result != null) {
            result.getVersions().sort(MinimalisticVersionDto.COMPARATOR_BY_INTERNAL_ORDER);
        }
        return new Errorable(result);
    }

    /**
     * Create new project from basic scratch.
     * Contains {@link MinimalisticProjectDto} in request body.
     * @param req default Spark request.
     * @param res default Spark response.
     * @return Errorable with "success" string.
     */
    @SneakyThrows
    public Errorable createProject(Request req, Response res) {
        String token = req.headers(Configurator.TOKEN_HEADER);
        UserToken userToken = Ebean.find(UserToken.class)
                .select("id, createDate")
                .fetch("user", "id, name, deleted")
                .where()
                .eq("token", UUID.fromString(token))
                .eq("deleted", false)
                .findOne();
        if (userToken == null) {
            log.error("Could not find user by token " + token);
            return new Errorable(null, "Server error happened");
        }
        MinimalisticProjectDto dto = mapper.readValue(req.body(), MinimalisticProjectDto.class);
        Project model = beanMapper.map(dto, Project.class);
        projectService.create(model, userToken.getUser());
        return new Errorable("success");
    }

    /**
     * Updates project's info.
     * Contains {@link MinimalisticProjectDto} in request body.
     * Does not update related entities to {@link Project}.
     * @param req default Spark request.
     * @param res default Spark response.
     * @return Errorable with the same project.
     */
    @SneakyThrows
    public Errorable updateProject(Request req, Response res) {
        MinimalisticProjectDto dto = mapper.readValue(req.body(), MinimalisticProjectDto.class);
        Project model = beanMapper.map(dto, Project.class);
        // Route is authorized before, so userToken is not null
        String token = req.headers(Configurator.TOKEN_HEADER);
        UserToken userToken = Ebean.find(UserToken.class)
                .select("id, createDate")
                .fetch("user", "id, name, deleted")
                .where()
                .eq("token", UUID.fromString(token))
                .eq("deleted", false)
                .findOne();

        Errorable result = projectService.update(model, userToken.getUser());
        if (result.getData() != null) {
            result.setData(mapper.writeValueAsString(result.getData()));
        }
        return result;
    }

    /**
     * Deletes project with all related entities.
     * Contains {@link MinimalisticProjectDto} in request body.
     * Things are hard deleted.
     * @param req default Spark request.
     * @param res default Spark response.
     * @return Errorable with the same project.
     */
    @SneakyThrows
    public Errorable deleteProject(Request req, Response res) {
        MinimalisticProjectDto dto = mapper.readValue(req.body(), MinimalisticProjectDto.class);
        Project model = beanMapper.map(dto, Project.class);
        String token = req.headers(Configurator.TOKEN_HEADER);
        // Route is authorized before, so userToken is not null
        UserToken userToken = Ebean.find(UserToken.class)
                .select("id, createDate")
                .fetch("user", "id, name, deleted")
                .where()
                .eq("token", UUID.fromString(token))
                .eq("deleted", false)
                .findOne();
        Errorable result = projectService.delete(model, userToken.getUser());
        if (result.getData() != null) {
            result.setData(mapper.writeValueAsString(result.getData()));
        }
        return result;
    }
}
