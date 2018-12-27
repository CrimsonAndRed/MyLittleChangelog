package my.little.changelog.model.project.dto;

import lombok.Getter;
import lombok.Setter;
import my.little.changelog.json.JsonDto;

import javax.annotation.Nonnull;
import java.util.Comparator;

/**
 * DTO for version without references to other model.
 */
@Getter
@Setter
public class MinimalisticVersionDto implements JsonDto {

    /**
     * Comparator for DTOs by internal number.
     * TODO alphanumeric instead of natural.
     */
    public static final Comparator<MinimalisticVersionDto> COMPARATOR_BY_INTERNAL_ORDER = Comparator.comparing(MinimalisticVersionDto::getInternalOrder, Comparator.naturalOrder());

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

    /**
     * Internal order of versions.
     */
    @Nonnull
    private Long internalOrder;
}
