package my.little.changelog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import my.little.changelog.config.Configurator;
import my.little.changelog.error.Errorable;
import my.little.changelog.json.JsonDto;
import my.little.changelog.model.auth.dto.LoginDto;
import my.little.changelog.model.auth.dto.LoginResponseDto;
import my.little.changelog.service.AuthService;
import spark.Request;
import spark.Response;

/**
 * Controls authentication process and other regular account-based problems.
 */
@Log4j2
@Singleton
public class AuthController {

    @Inject
    private ObjectMapper mapper;

    @Inject
    private AuthService authService;

    /**
     * Log user in.
     * Contains user login and password in body {@link LoginDto}.
     * @param req default Spark request.
     * @param res default Spark response.
     * @return User token or error.
     */
    @SneakyThrows
    public Errorable<LoginResponseDto> login(Request req, Response res) {

        String body = req.body();
        LoginDto loginDto = mapper.readValue(body, LoginDto.class);

        Errorable result = authService.login(loginDto);
        return result;
    }

    /**
     * Drops user session.
     * @param req default Spark request.
     * @param res default Spark response.
     * @return Success string.
     */
    public Errorable<Void> logout(Request req, Response res) {
        String token = req.headers(Configurator.TOKEN_HEADER);
        authService.logout(token);

        return new Errorable<>(null);
    }
}
