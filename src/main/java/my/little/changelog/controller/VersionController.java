package my.little.changelog.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.ebean.Ebean;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import my.little.changelog.config.Configurator;
import my.little.changelog.error.ErrorMessage;
import my.little.changelog.error.Errorable;
import my.little.changelog.global.OrikaMapper;
import my.little.changelog.model.auth.UserToken;
import my.little.changelog.model.project.Version;
import my.little.changelog.model.project.dto.CreateVersionDto;
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

    /**
     * Creates new version based on user input.
     * Contains {@link MinimalisticVersionDto} in request body with additional field projectId with identifier of project.
     * @param req default Spark request.
     * @param res default Spark response.
     * @return Errorable with the same version.
     */
    @SneakyThrows
    public Errorable createVersion(Request req, Response res) {
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
            return new Errorable(null, ErrorMessage.genericError());
        }

        Errorable result = versionService.createVersion(dto, userToken.getUser());
        if (result.getData() != null) {
            result.setData(beanMapper.map(result.getData(), MinimalisticVersionDto.class));
        }
        return result;
    }
}
