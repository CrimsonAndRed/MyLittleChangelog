package my.little.changelog.controller;

import com.google.common.base.Throwables;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.extern.log4j.Log4j2;
import my.little.changelog.error.Errorable;
import my.little.changelog.global.OrikaMapper;
import my.little.changelog.model.project.Version;
import my.little.changelog.model.project.dto.FullVersionDto;
import my.little.changelog.service.ChangelogService;
import spark.Request;
import spark.Response;

/**
 * Changelog-related things controller.
 */
@Singleton
@Log4j2
public class ChangelogController {

    @Inject
    private ChangelogService changelogService;

    @Inject
    private OrikaMapper beanMapper;

    /**
     * Find version ("timeslice") by id.
     * Returns full information of version (model {@link my.little.changelog.model.project.Version} can be saved based on this information) or error in case malformed parameter.
     * Has request parameters:
     * - id - identifier of version.
     * @param req default Spark request.
     * @param res default Spark response.
     * @return Errorable with one version data {@link my.little.changelog.model.project.dto.FullVersionDto}.
     */
    public Errorable<FullVersionDto> getFullVersion(Request req, Response res) {
        String paramId = req.params("id");
        Long id;
        if (paramId == null) {
            return new Errorable<>(null, "Request error: could not read id");
        } else {
            try {
                id = Long.valueOf(paramId);
            } catch (NumberFormatException e) {
                log.error(Throwables.getStackTraceAsString(e));
                return new Errorable<>(null, "Request error: could not parse number from " + paramId);
            }
        }
        Version version = changelogService.getFullVersionById(id);
        // Converter logic
        // Too complicated to do it with orika.
        FullVersionDto result = changelogService.createVersionDtoByVersion(version);
        return new Errorable<>(result);
    }
}
