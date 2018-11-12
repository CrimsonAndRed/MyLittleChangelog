package my.little.changelog.model;

import io.ebean.Model;
import io.ebean.annotation.WhenCreated;
import io.ebean.annotation.WhenModified;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.Nonnull;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import java.time.Instant;

/**
 * Class with basic fields, to be presented in all database models.
 */
@MappedSuperclass
@Getter
@Setter
public class BasicModel extends Model {

    /**
     * Model identifier.
     * Does not need sequence since it is handled by driver/database itself (serial type).
     */
    @Id
    @Column(name = "id")
    @Nonnull
    private Long id;

    /**
     * Model creation date.
     */
    @WhenCreated
    @Column(name = "create_date")
    @Nonnull
    private Instant createDate;

    /**
     * Model last update date.
     */
    @WhenModified
    @Column(name = "update_date")
    @Nonnull
    private Instant updateDate;

    /**
     * Version number.
     */
    @Version
    @Column(name = "version")
    @Nonnull
    private Long version;
}
