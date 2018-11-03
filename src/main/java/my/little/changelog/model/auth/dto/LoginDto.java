package my.little.changelog.model.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import my.little.changelog.json.JsonDto;

/**
 * DTO for user login page.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginDto implements JsonDto {

    /**
     * User login name.
     */
    private String login;

    /**
     * User password.
     */
    private String password;
}
