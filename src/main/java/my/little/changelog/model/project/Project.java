package my.little.changelog.model.project;

import lombok.Getter;
import lombok.Setter;
import my.little.changelog.model.BasicModel;
import my.little.changelog.model.auth.User;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
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
    @Nonnull
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
    @Nonnull
    private User createUser;

    /**
     * List of versions of project.
     */
    @OneToMany(mappedBy = "project", cascade = {CascadeType.REMOVE})
    @Nonnull
    private List<Version> versions;

    /**
     * List of versions of project.
     */
    @OneToMany(mappedBy = "project", cascade = {CascadeType.REMOVE})
    @Nonnull
    private List<Route> routes;
}
