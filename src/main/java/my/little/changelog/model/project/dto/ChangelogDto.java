package my.little.changelog.model.project.dto;

import lombok.Getter;
import lombok.Setter;
import my.little.changelog.json.JsonDto;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Dto for changelog unit.
 */
@Getter
@Setter
public class ChangelogDto implements JsonDto {

    /**
     * Identifier of changelog.
     */
    @Nonnull
    private Long id;

    /**
     * Virtual identifier of changelog.
     */
    @Nonnull
    private Long vid;

    /**
     *  Text of changelog.
     */
    @Nullable
    private String text;

    /**
     * Deleted flag.
     */
    @Nonnull
    private Boolean deleted;

    /**
     * Inner version.
     */
    @Nonnull
    private Long v;
}
