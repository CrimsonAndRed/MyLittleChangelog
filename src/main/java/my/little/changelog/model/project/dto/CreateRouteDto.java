package my.little.changelog.model.project.dto;

import lombok.Getter;
import lombok.Setter;
import my.little.changelog.annotation.NotNull;
import my.little.changelog.json.JsonDto;

/**
 * Transfer object to create new route for project.
 */
@Getter
@Setter
public class CreateRouteDto implements JsonDto {

    /**
     * Id of project, related to this route.
     */
    @NotNull
    private Long projectId;

    /**
     * Name of route.
     */
    @NotNull
    private String name;
}
