package my.little.changelog.model.auth;

import io.ebean.Model;
import io.ebean.annotation.SoftDelete;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * User token model.
 * Contains non-actual data.
 */
@Entity
@Table(name = "user_token")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserToken extends Model {

    /**
     * Token Id.
     */
    @Id
    @Column(name = "id")
    private Long id;

    /**
     * Token uuid value.
     */
    @Column(name = "token")
    @Nonnull
    private UUID token;

    /**
     * Token create date.
     */
    @Column(name = "create_date")
    @Nonnull
    private LocalDateTime createDate;

    /**
     * Token end date.
     */
    @Column(name = "end_date")
    @Nullable
    private LocalDateTime endDate;

    /**
     * Token holder.
     */
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @Nonnull
    private User user;

    /**
     * Soft delete marker.
     */
    @SoftDelete
    @Column(name = "is_deleted")
    @Nonnull
    private Boolean deleted;
}
