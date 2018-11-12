package my.little.changelog.model.business.dto;

import lombok.Getter;
import lombok.Setter;
import my.little.changelog.json.JsonDto;
import my.little.changelog.model.auth.dto.UserDto;

import javax.annotation.Nonnull;
import java.time.Instant;

/**
 * Dto for project information.
 */
@Getter
@Setter
public class ProjectDto implements JsonDto {

    /**
     * Project identifier.
     */
    @Nonnull
    private Long id;

    /**
     * Project name.
     */
    @Nonnull
    private String name;

    /**
     * Owner of project.
     */
    @Nonnull
    private UserDto createUser;

    /**
     * Creation date.
     */
    @Nonnull
    private Instant createDate;

    /**
     * Model version.
     */
    @Nonnull
    private Long version;
}
