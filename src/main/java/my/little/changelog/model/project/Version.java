package my.little.changelog.model.project;

import lombok.Getter;
import lombok.Setter;
import my.little.changelog.model.BasicModel;
import my.little.changelog.annotation.NotNull;
import my.little.changelog.annotation.Nullable;

import javax.persistence.*;
import java.util.List;

/**
 * Version of project.
 * Any project to be divided by timeline slice ("timeslice"), which is described by version.
 */
@Entity
@Table(name = "project_version")
@Getter
@Setter
public class Version extends BasicModel {

    /**
     * Number of version.
     * Can be like "12.3.412" or something like it, so it is not a number.
     */
    @Column(name = "num")
    @NotNull
    private String num;

    /**
     * Internal order of versions.
     * Project starts with version 0, each version increments order by 1.
     */
    @Column(name = "internal_order")
    @NotNull
    private Long internalOrder;

    /**
     * Description of version.
     */
    @Column(name = "description")
    @Nullable
    private String description;

    /**
     * Parent project.
     */
    @ManyToOne
    @JoinColumn(name = "project_id", referencedColumnName = "id")
    @NotNull
    private Project project;

    /**
     * Child changelogs of this timeslice.
     */
    @OneToMany(mappedBy = "version", cascade = {CascadeType.REMOVE})
    @NotNull
    private List<Changelog> changelogs;
}