package my.little.changelog.model.auth;

import io.ebean.Model;
import io.ebean.annotation.SoftDelete;
import io.ebean.annotation.WhenCreated;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
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
    @Nonnull
    private Long id;

    /**
     * Token creation date.
     */
    @WhenCreated
    @Column(name = "create_date")
    @Nonnull
    private Instant createDate;

    /**
     * Token uuid value.
     */
    @Column(name = "token")
    @Nonnull
    private UUID token;

    /**
     * Token holder.
     */
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @Nonnull
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
    @Nonnull
    private Boolean deleted;
}
