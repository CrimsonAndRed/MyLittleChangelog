package my.little.changelog.model.auth.dto;

import lombok.Getter;
import lombok.Setter;
import my.little.changelog.json.JsonDto;

import javax.annotation.Nonnull;

/**
 * DTO for User model.
 * Does not contain vital information like password or salt.
 */
@Getter
@Setter
public class UserDto implements JsonDto {

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

    /**
     * Model version.
     */
    @Nonnull
    private Long version;
}
