package my.little.changelog.service;

import com.google.inject.Singleton;
import io.ebean.Ebean;
import io.ebean.FetchConfig;
import lombok.extern.log4j.Log4j2;
import my.little.changelog.model.auth.User;
import my.little.changelog.model.business.Project;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * Service of project-related things.
 */
@Singleton
@Log4j2
public class ProjectService {

    /**
     * Find all projects, registered in system
     * TODO delete this because it cant be good in distance.
     * @return list of projects.
     */
    @Nonnull
    public List<Project> getAll() {
        List<Project> result = Ebean.find(Project.class)
                .fetch("createUser", new FetchConfig().query())
                .findList();
        return result;
    }

    /**
     * Find all projects, registered by giver user.
     * @param user owner of projects.
     * @return list of user's projects.
     */
    @Nonnull
    public List<Project> getByUser(User user) {
        List<Project> result = Ebean.find(Project.class)
                .fetch("createUser", new FetchConfig().query())
                .where()
                .eq("createUser.id", user.getId())
                .findList();
        return result;
    }

    /**
     * Find projects by it's id.
     * @param id id of project.
     * @return project or null.
     */
    @Nullable
    public Project getById(Long id) {
        Project result = Ebean.find(Project.class)
                .fetch("createUser", new FetchConfig().query())
                .where()
                .eq("id", id)
                .findOne();
        return result;
    }

    /**
     * Creates new project from user inputs.
     * @param project project object.
     * @return project object with id.
     */
    public Project create(Project project) {
        project.save();
        return project;
    }

    /**
     * Updates existing project from user inputs.
     * @param project project object.
     * @return the same saved project.
     */
    public Project update(Project project) {
        project.update();
        return project;
    }
}
