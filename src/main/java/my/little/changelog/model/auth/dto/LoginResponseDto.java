package my.little.changelog.model.auth.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

/**
 * Response for user logging in.
 */
public class LoginResponseDto {

    /**
     * New token.
     */
    @Getter
    @Setter
    private UUID token;

    /**
     * Username.
     */
    @Getter
    @Setter
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
    public LoginResponseDto(UUID token, String username) {
        this.token = token;
        this.username = username;
    }
}
