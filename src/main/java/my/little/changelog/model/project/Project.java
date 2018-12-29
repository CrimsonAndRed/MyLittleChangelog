package my.little.changelog.model.project;

import lombok.Getter;
import lombok.Setter;
import my.little.changelog.model.BasicModel;
import my.little.changelog.model.auth.User;
import my.little.changelog.annotation.NotNull;
import my.little.changelog.annotation.Nullable;

import javax.persistence.*;
import java.util.List;

/**
 * Project is a most generic unit.
 * It contains several versions ("timeslices" of project state).
 * Project is owned by one user.
 * TODO user should be able to give permission to change his project by some other users.
 */
@Entity
@Table(name = "project")
@Getter
@Setter
public class Project extends BasicModel {

    /**
     * Name of project.
     */
    @Column(name = "name")
    @NotNull
    private String name;

    /**
     * Description of project.
     */
    @Column(name = "description")
    @Nullable
    private String description;

    /**
     * Owner of project.
     */
    @ManyToOne
    @JoinColumn(name = "create_user", referencedColumnName = "id")
    @NotNull
    private User createUser;

    /**
     * List of versions of project.
     */
    @OneToMany(mappedBy = "project", cascade = {CascadeType.REMOVE})
    @NotNull
    private List<Version> versions;

    /**
     * List of versions of project.
     */
    @OneToMany(mappedBy = "project", cascade = {CascadeType.REMOVE})
    @NotNull
    private List<Route> routes;
}
