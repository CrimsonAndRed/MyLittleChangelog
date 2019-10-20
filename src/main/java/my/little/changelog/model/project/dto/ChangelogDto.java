package my.little.changelog.model.project.dto;

import lombok.Getter;
import lombok.Setter;
import my.little.changelog.json.JsonDto;
import my.little.changelog.annotation.NotNull;
import my.little.changelog.annotation.Nullable;

/**
 * Dto for changelog unit.
 */
@Getter
@Setter
public class ChangelogDto implements JsonDto {

    /**
     * Identifier of changelog.
     */
    @NotNull
    private Long id;

    /**
     * Virtual identifier of changelog.
     */
    @NotNull
    private Long vid;

    /**
     *  Text of changelog.
     */
    @Nullable
    private String text;

    /**
     * Deleted flag.
     */
    @NotNull
    private Boolean deleted;

    /**
     * Inner version.
     */
    @NotNull
    private Long v;
}