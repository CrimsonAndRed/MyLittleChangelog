package my.little.changelog.model.project.dto;

import lombok.Getter;
import lombok.Setter;
import my.little.changelog.json.JsonDto;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * Full dto for version with referenced children routes.
 * This dto is not handled by {@link my.little.changelog.global.OrikaMapper} because there is grouping logic behind mapping routes.
 */
@Getter
@Setter
public class FullVersionDto implements JsonDto {

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
     * Version description.
     */
    @Nullable
    private String description;

    /**
     * Internal order.
     */
    @Nonnull
    private Long internalOrder;

    /**
     * Child routes.
     */
    @Nonnull
    private List<RouteDto> routes;

    /**
     * Inner version.
     */
    @Nonnull
    private Long v;

    /**
     * Parent parent.
     * Might be null depending on possible needs.
     */
    @Nullable
    private MinimalisticProjectDto project;
}
