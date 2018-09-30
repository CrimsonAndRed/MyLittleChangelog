package my.little.changelog.model.auth;

import io.ebean.Model;
import io.ebean.annotation.SoftDelete;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * User model.
 * Contains non-actual data.
 */
@Entity
@Table(name = "app_user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User extends Model {

    /**
     * User id.
     */
    @Id
    @Column(name = "id")
    private Long id;

    /**
     * User name.
     */
    @Column(name = "name")
    @Nonnull
    private String name;

    /**
     * User password's hash.
     */
    @Column(name = "password")
    @Nonnull
    private byte[] password;

    /**
     * User's salt for hash.
     */
    @Column(name = "salt")
    @Nullable
    private byte[] salt;

    /**
     * User create date.
     */
    @Column(name = "create_date")
    @Nonnull
    private LocalDateTime createDate;

    /**
     * Soft delete marker.
     */
    @SoftDelete
    @Column(name = "is_deleted")
    @Nonnull
    private Boolean deleted;
}
