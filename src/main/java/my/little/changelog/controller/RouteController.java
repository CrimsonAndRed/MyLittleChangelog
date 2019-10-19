package my.little.changelog.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.ebean.Ebean;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import my.little.changelog.config.Configurator;
import my.little.changelog.error.Errorable;
import my.little.changelog.global.OrikaMapper;
import my.little.changelog.model.auth.UserToken;
import my.little.changelog.model.project.dto.CreateRouteDto;
import my.little.changelog.model.project.dto.RouteDto;
import my.little.changelog.service.RouteService;
import spark.Request;
import spark.Response;

import java.util.UUID;

@Singleton
@Log4j2
public class RouteController {

    @Inject
    private ObjectMapper mapper;

    @Inject
    private OrikaMapper beanMapper;

    @Inject
    private RouteService routeService;

    /**
     * Create ne route for a given project
     * @param req default Spark request.
     * @param res default Spark response.
     * @return new route model dto.
     */
    @SneakyThrows
    public Errorable<RouteDto> createRoute(Request req, Response res) {
        String token = req.headers(Configurator.TOKEN_HEADER);
        UserToken userToken = Ebean.find(UserToken.class)
                .select("id, createDate")
                .fetch("user", "id, name, deleted")
                .where()
                .eq("token", UUID.fromString(token))
                .eq("deleted", false)
                .findOne();

        if (userToken == null) {
            return new Errorable<>(null, "You are not permitted to update this project");
        }

        JsonNode node = mapper.readTree(req.body());
        CreateRouteDto dto = mapper.treeToValue(node, CreateRouteDto.class);

        Errorable<RouteDto> result = routeService.create(dto, userToken.getUser()).map(i -> beanMapper.map(i, RouteDto.class));
        return result;
    }
}