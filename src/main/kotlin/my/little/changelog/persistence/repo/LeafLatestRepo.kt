package my.little.changelog.persistence.repo

import my.little.changelog.model.auth.User
import my.little.changelog.model.leaf.LeafLatest
import my.little.changelog.model.leaf.LeavesLatest
import my.little.changelog.model.project.Project
import my.little.changelog.persistence.AbstractCrudRepository
import org.jetbrains.exposed.sql.SizedIterable
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.not
import org.jetbrains.exposed.sql.statements.jdbc.iterate
import org.jetbrains.exposed.sql.transactions.transaction

object LeafLatestRepo : AbstractCrudRepository<LeafLatest, Int>(LeafLatest) {

    private const val FIND_LEAVES_BY_PROJECT_QUERY =
        """
           SELECT leaves_latest.id FROM leaves_latest
           JOIN versions ON leaves_latest.version_id = versions.id
           WHERE versions.project_id = ? AND leaves_latest.is_deleted <> true
        """

    fun findAllByProjectNotDeleted(project: Project): SizedIterable<LeafLatest> = transaction {
        raw(FIND_LEAVES_BY_PROJECT_QUERY, arrayOf("id")) {
            set(1, project.id.value)
        }.iterate { getInt("id") }.let { LeafLatest.forIds(it) }
    }

    fun findAllByGroupsNotDeleted(groupVids: Iterable<Int>): SizedIterable<LeafLatest> = transaction {
        LeafLatest.find { (LeavesLatest.groupVid inList groupVids) and not(LeavesLatest.isDeleted eq true) }
    }
}
