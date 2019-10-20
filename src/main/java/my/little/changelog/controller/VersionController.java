package my.little.changelog.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Throwables;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.ebean.Ebean;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import my.little.changelog.config.Configurator;
import my.little.changelog.error.ErrorMessage;
import my.little.changelog.error.Errorable;
import my.little.changelog.global.OrikaMapper;
import my.little.changelog.merger.VersionMerger;
import my.little.changelog.model.auth.UserToken;
import my.little.changelog.model.project.Version;
import my.little.changelog.model.project.dto.CreateVersionDto;
import my.little.changelog.model.project.dto.FullVersionDto;
import my.little.changelog.model.project.dto.MinimalisticVersionDto;
import my.little.changelog.service.VersionService;
import spark.Request;
import spark.Response;

import java.util.UUID;

/**
 * Versions-related controller.
 */
@Log4j2
@Singleton
public class VersionController {

    @Inject
    private ObjectMapper mapper;

    @Inject
    private OrikaMapper beanMapper;

    @Inject
    private VersionService versionService;

    @Inject
    private VersionMerger versionMerger;

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
            return new Errorable<>(null, ErrorMessage.couldNotParse("id", "null"));
        } else {
            try {
                id = Long.valueOf(paramId);
            } catch (NumberFormatException e) {
                log.error(Throwables.getStackTraceAsString(e));
                return new Errorable<>(null, ErrorMessage.couldNotParse("id", paramId));
            }
        }
        Version version = versionService.getFullVersionById(id);
        // Converter logic
        // Too complicated to do it with orika.
        FullVersionDto result = versionService.createVersionDtoByVersion(version);
        return new Errorable<>(result);
    }

    /**
     * Creates new version based on user input.
     * Contains {@link MinimalisticVersionDto} in request body with additional field projectId with identifier of project.
     * @param req default Spark request.
     * @param res default Spark response.
     * @return Errorable with the same version.
     */
    @SneakyThrows
    public Errorable<MinimalisticVersionDto> createVersion(Request req, Response res) {
        JsonNode node = mapper.readTree(req.body());
        CreateVersionDto dto = mapper.treeToValue(node, CreateVersionDto.class);

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
            return new Errorable<>(null, ErrorMessage.genericError());
        }

        Errorable result = versionService.createVersion(dto, userToken.getUser());
        if (result.getData() != null) {
            result.setData(beanMapper.map(result.getData(), MinimalisticVersionDto.class));
        }
        return result;
    }

    /**
     * Reorder versions: giver version in request
     * @param req default Spark request.
     * @param res default Spark response.
     * @return Errorable: empty or with errors.
     */
    @SneakyThrows
    public MinimalisticVersionDto moveVersion(Request req, Response res) {
        JsonNode node = mapper.readTree(req.body());
        MinimalisticVersionDto dto = mapper.treeToValue(node, MinimalisticVersionDto.class);

        Long newOrder = Long.parseLong(req.params("newOrder"));
        Version version = beanMapper.map(dto, Version.class);


        MinimalisticVersionDto updatedDto = beanMapper.map(versionService.moveVersion(newOrder, version), MinimalisticVersionDto.class);
        return updatedDto;
    }

    /**
     * Delete version by given id.
     * @param req default Spark request.
     * @param res default Spark response.
     * @return Errorable: empty or with errors.
     */
    @SneakyThrows
    public Errorable<Void> deleteVersion(Request req, Response res) {
        JsonNode node = mapper.readTree(req.body());
        FullVersionDto dto = mapper.treeToValue(node, FullVersionDto.class);
        String token = req.headers(Configurator.TOKEN_HEADER);
        // Route is authorized before, so userToken should not be null (unless user was deleted in between queries)
        UserToken userToken = Ebean.find(UserToken.class)
                .select("id, createDate")
                .fetch("user", "id, name, deleted")
                .where()
                .eq("token", UUID.fromString(token))
                .eq("deleted", false)
                .findOne();
        if (userToken == null) {
            log.error("Could not find user by token " + token);
            return new Errorable<>(null, ErrorMessage.genericError());
        }

        String paramId = req.params("id");
        Long id;
        if (paramId == null) {
            return new Errorable<>(null, ErrorMessage.couldNotParse("id", "null"));
        } else {
            try {
                id = Long.valueOf(paramId);
            } catch (NumberFormatException e) {
                log.error(Throwables.getStackTraceAsString(e));
                return new Errorable<>(null, ErrorMessage.couldNotParse("id", paramId));
            }
        }
        Errorable<Void> result = versionService.deleteVersion(id, userToken.getUser());
        return result;
    }

    /**
     * Validate and save version.
     * @param req default Spark request.
     * @param res default Spark response.
     * @return Errorable: empty or with errors.
     */
    @SneakyThrows
    public Errorable<Void> saveVersion(Request req, Response res) {
        JsonNode node = mapper.readTree(req.body());
        FullVersionDto dto = mapper.treeToValue(node, FullVersionDto.class);
        String token = req.headers(Configurator.TOKEN_HEADER);
        // Route is authorized before, so userToken should not be null (unless user was deleted in between queries)
        UserToken userToken = Ebean.find(UserToken.class)
                .select("id, createDate")
                .fetch("user", "id, name, deleted")
                .where()
                .eq("token", UUID.fromString(token))
                .eq("deleted", false)
                .findOne();

        if (userToken == null) {
            log.error("Could not find user by token " + token);
            return new Errorable<>(null, ErrorMessage.genericError());
        }

        String paramId = req.params("id");
        Long id;
        if (paramId == null) {
            return new Errorable<>(null, ErrorMessage.couldNotParse("id", "null"));
        } else {
            try {
                id = Long.valueOf(paramId);
            } catch (NumberFormatException e) {
                log.error(Throwables.getStackTraceAsString(e));
                return new Errorable<>(null, ErrorMessage.couldNotParse("id", paramId));
            }
        }

        Errorable<Version> version = versionMerger.mergeFull(id, dto);
        if (!version.getErrors().isEmpty()) {
            return version.toPrimitiveErrorable();
        }

        Errorable<Version> result = versionService.saveVersion(version.getData(), userToken.getUser());
        return result.toPrimitiveErrorable();
    }
}