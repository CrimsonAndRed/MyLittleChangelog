package my.little.changelog.model.project.dto;

import lombok.Getter;
import lombok.Setter;
import my.little.changelog.json.JsonDto;
import my.little.changelog.model.auth.dto.MinimalisticUserDto;

import javax.annotation.Nonnull;
import java.util.Comparator;

/**
 * Dto for minimalistic project information.
 * Contains minimal information about project.
 * Can be used lists of projects (search, projects by owner etc).
 */
@Getter
@Setter
public class MinimalisticProjectDto implements JsonDto {

    /**
     * Comparator for DTOs by id).
     * It is implied that create date has same order as given id's.
     */
    public static final Comparator<MinimalisticProjectDto> COMPARATOR_BY_ID = Comparator.comparing(MinimalisticProjectDto::getId, Comparator.naturalOrder());

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
     * Project description.
     */
    @Nonnull
    private String description;

    /**
     * Owner of project.
     */
    @Nonnull
    private MinimalisticUserDto createUser;
}
