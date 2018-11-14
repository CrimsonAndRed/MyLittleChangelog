package my.little.changelog.model.auth.dto;

import lombok.Getter;
import lombok.Setter;
import my.little.changelog.json.JsonDto;

import javax.annotation.Nonnull;

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
    @Nonnull
    private Long id;

    /**
     * Username.
     */
    @Nonnull
    private String name;
}
