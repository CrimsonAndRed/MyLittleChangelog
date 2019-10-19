package my.little.changelog.service;

import com.google.inject.Singleton;
import io.ebean.Ebean;
import io.ebean.FetchConfig;
import lombok.extern.log4j.Log4j2;
import my.little.changelog.error.Errorable;
import my.little.changelog.json.JsonDto;
import my.little.changelog.model.auth.User;
import my.little.changelog.model.project.Project;

import my.little.changelog.annotation.Nullable;
import my.little.changelog.annotation.NotNull;
import java.util.List;

/**
 * Service of project-related things.
 */
@Singleton
@Log4j2
public class ProjectService {

    /**
     * Find all projects, registered in system
     * Fetches only user.
     * TODO delete this because it cant be good in distance.
     * @return list of projects.
     */
    @NotNull
    public List<Project> getMinimalisticAll() {
        List<Project> result = Ebean.find(Project.class)
                .select("id, name")
                .fetch("createUser", "id, name", new FetchConfig().query())
                .findList();
        return result;
    }

    /**
     * Find all projects, registered by giver user.
     * Fetches only user.
     * @param user owner of projects.
     * @return list of user's projects.
     */
    @NotNull
    public List<Project> getMinimalisticByUser(User user) {
        List<Project> result = Ebean.find(Project.class)
                .select("id, name")
                .fetch("createUser", "id, name", new FetchConfig().query())
                .where()
                .eq("createUser.id", user.getId())
                .findList();
        return result;
    }

    /**
     * Find projects by it's id.
     * Fetches only user.
     * @param id id of project.
     * @return project or null.
     */
    @Nullable
    public Project getMinimalisticById(Long id) {
        Project result = Ebean.find(Project.class)
                .select("id, name")
                .fetch("createUser", "id, name", new FetchConfig().query())
                .where()
                .eq("id", id)
                .findOne();
        return result;
    }

    /**
     * Find projects by it's id.
     * Fetches user and minimalistic version.
     * @param id id of project.
     * @return project or null.
     */
    @Nullable
    public Project getFullById(Long id) {
        Project result = Ebean.find(Project.class)
                .fetch("createUser", new FetchConfig().query())
                .fetch("versions", new FetchConfig().query())
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
    public Project create(Project project, User user) {
        project.setCreateUser(user);
        project.save();
        return project;
    }

    /**
     * Updates existing project from user inputs.
     * Checks user rights to manipulate with this project.
     * @param project project object.
     * @return Errorable with the same saved project.
   <  */
    public Errorable<Project> update(Project project, User user) {
        if (this.checkUserIsOwner(project.getId(), user.getId())) {
            project.update();
            return new Errorable<>(project);
        } else {
            return new Errorable<>(null, "You are not permitted to update this project");
        }
    }

    /**
     * Deletes project by id.
     * Checks user rights to manipulate with this project.
     * @param project project object.
     * @return Errorable with null data.
     */
    public Errorable<Void> delete(Project project, User user) {
        if (this.checkUserIsOwner(project.getId(), user.getId())) {
            project.delete();
            return new Errorable<>();
        } else {
            return new Errorable<>(null, "You are not permitted to update this project");
        }
    }

    /**
     * Checks that user is owner of the project.
     * @param projectId identifier of project.
     * @param userId identifier of user.
     * @return true if user is owner of project, false otherwise.
     */
    public boolean checkUserIsOwner(Long projectId, Long userId) {
        int cnt = Ebean.find(Project.class)
                .where()
                .eq("id", projectId)
                .eq("createUser.id", userId)
                .findCount();
        return cnt == 1;
    }
}
