package my.little.changelog.model.project.dto;

import lombok.Getter;
import lombok.Setter;
import my.little.changelog.json.JsonDto;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Dto for route.
 * Contains no referenced entities.
 */
@Getter
@Setter
public class RouteDto implements JsonDto {

    /**
     * Identifier of route.
     */
    @Nonnull
    private Long id;

    /**
     * Name of route.
     */
    @Nonnull
    private String name;

    /**
     * Inner version of model.
     */
    @Nonnull
    private Long v;

    /**
     * List of child changelogs.
     */
    @Nonnull
    private List<ChangelogDto> changelogs;
}
