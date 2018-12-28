package my.little.changelog.model;

import io.ebean.Model;
import io.ebean.annotation.WhenCreated;
import io.ebean.annotation.WhenModified;
import lombok.Getter;
import lombok.Setter;
import my.little.changelog.annotation.NotNull;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import java.time.Instant;

/**
 * Class with basic fields, to be presented in most of basic database models.
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
    @my.little.changelog.annotation.NotNull
    private Long id;

    /**
     * Model creation date.
     */
    @WhenCreated
    @Column(name = "create_date")
    @NotNull
    private Instant createDate;

    /**
     * Model last update date.
     */
    @WhenModified
    @Column(name = "update_date")
    @NotNull
    private Instant updateDate;

    /**
     * Inner version number.
     * It avoid some bad situation, where OptimisticLock is raised.
     */
    @Version
    @Column(name = "v")
    @NotNull
    private Long v;
}
