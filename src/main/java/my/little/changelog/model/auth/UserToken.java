package my.little.changelog.model.auth;

import io.ebean.Model;
import io.ebean.annotation.SoftDelete;
import io.ebean.annotation.WhenCreated;
import lombok.Getter;
import lombok.Setter;
import my.little.changelog.annotation.NotNull;
import my.little.changelog.annotation.Nullable;

import javax.persistence.*;
import java.time.Instant;
import java.util.UUID;

/**
 * User token model.
 * Contains non-actual data.
 */
@Entity
@Table(name = "user_token")
@Getter
@Setter
public class UserToken extends Model {

    /**
     * Token identifier.
     */
    @Id
    @Column(name = "id")
    @NotNull
    private Long id;

    /**
     * Token creation date.
     */
    @WhenCreated
    @Column(name = "create_date")
    @NotNull
    private Instant createDate;

    /**
     * Token uuid value.
     */
    @Column(name = "token")
    @NotNull
    private UUID token;

    /**
     * Token holder.
     */
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @NotNull
    private User user;

    /**
     * Date of token invalidation.
     */
    @Column(name = "end_date")
    @Nullable
    private Instant endDate;

    /**
     * Soft delete marker.
     */
    @SoftDelete
    @Column(name = "is_deleted")
    @NotNull
    private Boolean deleted;
}
