package my.little.changelog.merger;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.ebean.Ebean;
import lombok.extern.log4j.Log4j2;
import my.little.changelog.error.CustomError;
import my.little.changelog.error.Errorable;
import my.little.changelog.model.project.Changelog;
import my.little.changelog.model.project.Route;
import my.little.changelog.model.project.Version;
import my.little.changelog.model.project.dto.ChangelogDto;
import my.little.changelog.model.project.dto.FullVersionDto;
import my.little.changelog.model.project.dto.RouteDto;
import my.little.changelog.service.VersionService;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Merging service.
 * Maps dto to model in correct way.
 */
@Singleton
@Log4j2
public class VersionMerger {

    @Inject
    private VersionService versionService;

    /**
     * Mapping function.
     * @param id identifier of version.
     * @param dto transfer model of version.
     * @return correct model of version.
     */
    public Errorable<Version> mergeFull(Long id, FullVersionDto dto) {

        Version model = versionService.getFullVersionById(id);

        if (model == null) {
            String error = "Malformed request: could not find version by id " + id;
            log.error(error);
            return new Errorable<>(null, error);
        }

        List<CustomError> errors = new ArrayList<>();

        model.setDescription(dto.getDescription());
        model.setV(dto.getV());

        Map<Long, Changelog> dbChangelogs = model.getChangelogs().stream().collect(Collectors.toMap(
                item -> item.getId(),
                item -> item
        ));

        Map<Long, Route> dbRoutes = model.getProject().getRoutes().stream().collect(Collectors.toMap(
                item -> item.getId(),
                item -> item
        ));

        Set<Long> keepIds = new HashSet<>();

        for (RouteDto route : dto.getRoutes()) {
            for (ChangelogDto changelog : route.getChangelogs()) {

                Changelog changelogModel;
                if (changelog.getId() == null) {
                    Route dbRoute = dbRoutes.get(route.getId());
                    if (dbRoute == null) {
                        String error = "Missed route by id " + id + " in version " + model.getId();
                        log.error(error);
                        errors.add(new CustomError(error));
                        continue;
                    }

                    changelogModel = new Changelog();
                    changelogModel.setRoute(dbRoute);
                    changelogModel.setVersion(model);
                    model.getChangelogs().add(changelogModel);
                } else {
                    keepIds.add(changelog.getId());
                    changelogModel = dbChangelogs.get(changelog.getId());
                    if (changelogModel == null) {
                        String error = "Missed changelog by id " + id + " in version " + model.getId();
                        log.error(error);
                        errors.add(new CustomError(error));
                        continue;
                    }
                }

                changelogModel.setVid(changelog.getVid());
                changelogModel.setText(changelog.getText());
            }
        }

        Iterator<Changelog> it = model.getChangelogs().iterator();
        while (it.hasNext()) {
            Changelog changelog = it.next();
            if (changelog.getId() != null && !keepIds.contains(changelog.getId())) {
                it.remove();
            }
        }
        return new Errorable<>(model, errors);
    }
}