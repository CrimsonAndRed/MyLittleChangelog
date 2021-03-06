package my.little.changelog.persistence.repo

import my.little.changelog.model.auth.User
import my.little.changelog.model.version.Version
import my.little.changelog.model.version.Versions
import my.little.changelog.persistence.AbstractCrudRepository
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object VersionRepo : AbstractCrudRepository<Version, Int>(Version) {

    fun findLatest(): Version = transaction {
        Version.find { Versions.id eq findMaxVersionId() }.first()
    }

    fun findLatestByUser(user: User): Version = transaction {
        Version.find { Versions.id eq findMaxVersionIdByUser(user) }.first()
    }

    fun findAllByUser(user: User): SizedIterable<Version> = transaction {
        Version.find { Versions.user eq user.id }
    }

    // TODO Exception?
    private fun findMaxVersionIdByUser(user: User): Int = transaction {
        Version.find { Versions.user eq user.id }.maxOf { it.id }.value
    }

    private fun findMaxVersionId(): Int? = transaction {
        Versions.id.max().let {
            Versions.slice(it).selectAll().first()[it]?.value
        }
    }
}
