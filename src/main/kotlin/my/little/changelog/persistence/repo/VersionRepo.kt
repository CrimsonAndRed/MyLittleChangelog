package my.little.changelog.persistence.repo

import my.little.changelog.model.version.Version
import my.little.changelog.model.version.Versions
import my.little.changelog.persistence.AbstractCrudRepository
import org.jetbrains.exposed.sql.max
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

object VersionRepo : AbstractCrudRepository<Version, Int>(Version) {

    fun findLatest(): Version = transaction {
        Version.find { Versions.id eq findMaxVersionId() }.first()
    }

    private fun findMaxVersionId(): Int? = transaction {
        Versions.id.max().let {
            Versions.slice(it).selectAll().first()[it]?.value
        }
    }
}
