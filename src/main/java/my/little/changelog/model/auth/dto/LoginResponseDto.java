package my.little.changelog.model.auth.dto;

import lombok.Getter;
import lombok.Setter;
import my.little.changelog.annotation.NotNull;

import java.util.UUID;

/**
 * Response for user logging in.
 */
@Getter
@Setter
public class LoginResponseDto {

    /**
     * New token.
     */
    @NotNull
    private UUID token;

    /**
     * Username.
     */
    @NotNull
    private String username;

    /**
     * No args constructor.
     */
    public LoginResponseDto() {
    }

    /**
     * All args constructor.
     * @param token token.
     * @param username username.
     */
    public LoginResponseDto(@NotNull UUID token, @NotNull String username) {
        this.token = token;
        this.username = username;
    }
}
