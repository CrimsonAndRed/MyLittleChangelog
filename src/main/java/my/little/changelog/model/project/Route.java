package my.little.changelog.model.project;

import lombok.Getter;
import lombok.Setter;
import my.little.changelog.model.BasicModel;

import javax.persistence.*;

/**
 * Route that group several changelogs up.
 * Not a middle layer between project and changelog, but a grouping factor.
 */
@Entity
@Table(name = "changelog_route")
@Getter
@Setter
public class Route extends BasicModel {

    /**
     * Name of route.
     * Should be like "/root/something1/something2..."
     */
    @Column(name = "name")
    private String name;

    /**
     * Project - owner of route.
     */
    @ManyToOne
    @JoinColumn(name = "project_id", referencedColumnName = "id")
    private Project project;
}
