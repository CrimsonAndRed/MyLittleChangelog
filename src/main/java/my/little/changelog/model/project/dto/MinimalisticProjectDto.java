package my.little.changelog.model.project.dto;

import lombok.Getter;
import lombok.Setter;
import my.little.changelog.json.JsonDto;
import my.little.changelog.model.auth.dto.MinimalisticUserDto;

import javax.annotation.Nonnull;

/**
 * Dto for minimalistic project information.
 * Contains minimal information about project.
 * Can be used lists of projects (search, projects by owner etc).
 */
@Getter
@Setter
public class MinimalisticProjectDto implements JsonDto {

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
    private MinimalisticUserDto createUser;
}
