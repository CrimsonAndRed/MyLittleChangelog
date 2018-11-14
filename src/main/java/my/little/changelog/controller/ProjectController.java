package my.little.changelog.controller;

import com.google.common.base.Throwables;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.ebean.Ebean;
import lombok.extern.log4j.Log4j2;
import my.little.changelog.config.Configurator;
import my.little.changelog.error.Errorable;
import my.little.changelog.global.OrikaMapper;
import my.little.changelog.model.auth.UserToken;
import my.little.changelog.model.project.Project;
import my.little.changelog.model.project.dto.FullProjectDto;
import my.little.changelog.model.project.dto.MinimalisticProjectDto;
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

    /**
     * Find all projects in system.
     * @param req default Spark request.
     * @param res default Spark response.
     * @return list of all projects.
     */
    public List<MinimalisticProjectDto> getAllProjects(Request req, Response res) {
        List<Project> allProjects = projectService.getMinimalisticAll();
        List<MinimalisticProjectDto> result = beanMapper.mapAsList(allProjects, MinimalisticProjectDto.class);
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
        return new Errorable(result);
    }
}
