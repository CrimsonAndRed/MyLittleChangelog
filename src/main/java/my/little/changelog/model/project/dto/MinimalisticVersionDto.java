package my.little.changelog.model.project.dto;

import lombok.Getter;
import lombok.Setter;
import my.little.changelog.json.JsonDto;

import javax.annotation.Nonnull;

/**
 * DTO for version without references to other model.
 */
@Getter
@Setter
public class MinimalisticVersionDto implements JsonDto {

    /**
     * Identifier of version.
     */
    @Nonnull
    private Long id;

    /**
     * Version number.
     */
    @Nonnull
    private String num;
}
