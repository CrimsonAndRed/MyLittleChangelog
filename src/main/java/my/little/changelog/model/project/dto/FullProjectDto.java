package my.little.changelog.model.project.dto;

import lombok.Getter;
import lombok.Setter;
import my.little.changelog.json.JsonDto;
import my.little.changelog.model.auth.dto.MinimalisticUserDto;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * Full information about project.
 * Contains versions of project.
 */
@Getter
@Setter
public class FullProjectDto implements JsonDto {

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
     * Description of project.
     */
    @Nullable
    private String description;

    /**
     * Inner version.
     */
    @Nonnull
    private Long v;

    /**
     * Owner of project.
     */
    @Nonnull
    private MinimalisticUserDto createUser;

    /**
     * Several versions for project.
     */
    @Nonnull
    private List<MinimalisticVersionDto> versions;
}