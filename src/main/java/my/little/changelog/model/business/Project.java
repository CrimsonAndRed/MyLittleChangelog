package my.little.changelog.model.business;

import lombok.Getter;
import lombok.Setter;
import my.little.changelog.model.BasicModel;
import my.little.changelog.model.auth.User;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.persistence.*;

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
}
