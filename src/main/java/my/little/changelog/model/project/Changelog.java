package my.little.changelog.model.project;

import lombok.Getter;
import lombok.Setter;
import my.little.changelog.model.BasicModel;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.persistence.*;

/**
 * Smallest unit of project changelog.
 * This is core component in whole system.
 * One changelog is a piece of project state in an exact "timeslice".
 * These changelogs are grouped in routes.
 */
@Entity
@Table(name = "changelog")
@Getter
@Setter
public class Changelog extends BasicModel {

    /**
     * Time-independent identifier.
     * Feature that introduced in version 1 and changed in 1.1 has different changelog ids but same vids.
     */
    @Column(name = "vid")
    @Nonnull
    private Long vid;

    /**
     * Text of changelog.
     */
    @Column(name = "text")
    @Nullable
    private String text;

    /**
     * Version of changelog.
     */
    @ManyToOne
    @JoinColumn(name = "version_id", referencedColumnName = "id")
    @Nonnull
    private Version version;

    /**
     * Route of changelog.
     */
    @ManyToOne
    @JoinColumn(name = "route_id", referencedColumnName = "id")
    @Nonnull
    private Route route;

    /**
     * Endpoint flag of changelog.
     */
    @Column(name = "is_deleted")
    @Nonnull
    private Boolean deleted;
}
