package my.little.changelog.model.auth.dto;

import lombok.Getter;
import lombok.Setter;
import my.little.changelog.json.JsonDto;
import my.little.changelog.annotation.NotNull;

/**
 * User model with minimal information.
 * Does not contain vital information like password or salt.
 */
@Getter
@Setter
public class MinimalisticUserDto implements JsonDto {

    /**
     * User identifier.
     */
    @NotNull
    private Long id;

    /**
     * Username.
     */
    @NotNull
    private String name;
}
