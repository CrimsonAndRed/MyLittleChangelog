package my.little.changelog.persistence.repo

import my.little.changelog.model.auth.User
import my.little.changelog.model.auth.Users
import my.little.changelog.persistence.AbstractCrudRepository
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.statements.api.ExposedBlob
import org.jetbrains.exposed.sql.transactions.transaction

object UserRepo : AbstractCrudRepository<User, Int>(User) {

    fun findByLoginAndHash(login: String, hash: ByteArray): User? = transaction {
        User.find { (Users.login eq login) and (Users.password eq ExposedBlob(hash)) }.firstOrNull()
    }
}
