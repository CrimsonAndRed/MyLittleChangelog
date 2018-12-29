package my.little.changelog.model.project.dto;

import lombok.Getter;
import lombok.Setter;
import my.little.changelog.annotation.NotNull;
import my.little.changelog.json.JsonDto;

/**
 * User data to create new version for a specific project.
 */
@Getter
@Setter
public class CreateVersionDto implements JsonDto {

    /**
     * Project Id.
     */
    @NotNull
    private Long projectId;

    /**
     * Name of version.
     */
    @NotNull
    private String num;
}
