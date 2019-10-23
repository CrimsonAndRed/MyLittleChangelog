package my.little.changelog.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.ebean.Ebean;
import lombok.extern.log4j.Log4j2;
import my.little.changelog.error.Errorable;
import my.little.changelog.model.auth.User;
import my.little.changelog.model.project.Project;
import my.little.changelog.model.project.Route;
import my.little.changelog.model.project.dto.CreateRouteDto;

@Singleton
@Log4j2
public class RouteService {

    @Inject
    private ProjectService projectService;

    /**
     * Creates new route in project.
     * Checks user rights to manipulate with this project and for unique name.
     * @param dto route object.
     * @return route object with id.
     */
    public Errorable<Route> create(CreateRouteDto dto, User user) {
        if (!projectService.checkUserIsOwner(dto.getProjectId(), user.getId())) {
            return new Errorable<>(null, "You are not permitted to update this project");
        }

        Project project = Ebean.find(Project.class).setId(dto.getProjectId()).findOne();
        int count = Ebean.find(Route.class)
                .where()
                .eq("name", dto.getName())
                .eq("project.id", project.getId())
                .findCount();

        if (count > 0) {
            return new Errorable<>(null, "Name " + dto.getName() + " is not unique");
        }

        Route route = new Route();
        route.setName(dto.getName());
        route.setProject(project);
        route.save();
        return new Errorable<>(route);
    }
}
