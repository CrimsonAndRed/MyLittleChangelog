package my.little.changelog.model.project.dto;

import lombok.Getter;
import lombok.Setter;
import my.little.changelog.json.JsonDto;
import my.little.changelog.annotation.NotNull;

import java.util.ArrayList;
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
    @NotNull
    private Long id;

    /**
     * Name of route.
     */
    @NotNull
    private String name;

    /**
     * Inner version of model.
     */
    @NotNull
    private Long v;

    /**
     * List of child changelogs.
     */
    @NotNull
    private List<ChangelogDto> changelogs = new ArrayList<>();
}
