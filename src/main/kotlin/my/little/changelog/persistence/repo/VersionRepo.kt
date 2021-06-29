package my.little.changelog.persistence.repo

import my.little.changelog.model.project.Project
import my.little.changelog.model.version.Version
import my.little.changelog.model.version.Versions
import my.little.changelog.persistence.AbstractCrudRepository
import org.jetbrains.exposed.sql.SizedIterable
import org.jetbrains.exposed.sql.transactions.transaction

object VersionRepo : AbstractCrudRepository<Version, Int>(Version) {
    fun findLatestByProject(project: Project): Version = transaction {
        Version.find { Versions.id eq findMaxVersionIdByProject(project) }.first()
    }

    private fun findMaxVersionIdByProject(project: Project): Int = transaction {
        Version.find { Versions.project eq project.id }.maxOf { it.id }.value
    }

    fun findByProject(project: Project): SizedIterable<Version> = transaction {
        Version.find { Versions.project eq project.id }
    }
}
