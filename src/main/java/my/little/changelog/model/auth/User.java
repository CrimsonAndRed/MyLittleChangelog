package my.little.changelog.model.auth;

import io.ebean.annotation.SoftDelete;
import lombok.Getter;
import lombok.Setter;
import my.little.changelog.model.BasicModel;
import my.little.changelog.annotation.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * User model.
 * Contains non-actual data.
 */
@Entity
@Table(name = "app_user")
@Getter
@Setter
public class User extends BasicModel {

    /**
     * User name.
     */
    @Column(name = "name")
    @NotNull
    private String name;

    /**
     * User password's hash.
     */
    @Column(name = "password")
    @NotNull
    private byte[] password;

    /**
     * User's salt for hash.
     */
    @Column(name = "salt")
    @NotNull
    private byte[] salt;

    /**
     * Soft delete marker.
     */
    @SoftDelete
    @Column(name = "is_deleted", columnDefinition = "bool default false")
    @NotNull
    private Boolean deleted;
}
