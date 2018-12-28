package my.little.changelog.model.project.dto;

import lombok.Getter;
import lombok.Setter;
import my.little.changelog.json.JsonDto;
import my.little.changelog.model.auth.dto.MinimalisticUserDto;
import my.little.changelog.annotation.NotNull;
import my.little.changelog.annotation.Nullable;

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
    @NotNull
    private Long id;

    /**
     * Project name.
     */
    @NotNull
    private String name;

    /**
     * Description of project.
     */
    @Nullable
    private String description;

    /**
     * Inner version.
     */
    @NotNull
    private Long v;

    /**
     * Owner of project.
     */
    @NotNull
    private MinimalisticUserDto createUser;

    /**
     * Several versions for project.
     */
    @NotNull
    private List<MinimalisticVersionDto> versions;
}