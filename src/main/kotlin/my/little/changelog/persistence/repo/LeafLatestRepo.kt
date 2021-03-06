package my.little.changelog.persistence.repo

import my.little.changelog.model.auth.User
import my.little.changelog.model.leaf.LeafLatest
import my.little.changelog.persistence.AbstractCrudRepository
import org.jetbrains.exposed.sql.SizedIterable
import org.jetbrains.exposed.sql.statements.jdbc.iterate
import org.jetbrains.exposed.sql.transactions.transaction

object LeafLatestRepo : AbstractCrudRepository<LeafLatest, Int>(LeafLatest) {

    private const val FIND_LEAVES_BY_USER_QUERY =
        """
           SELECT leaves_latest.id FROM leaves_latest
           JOIN versions ON leaves_latest.version_id = versions.id
           WHERE versions.user_id = ?
        """

    fun findAllByUser(user: User): SizedIterable<LeafLatest> = transaction {
        connection.prepareStatement(FIND_LEAVES_BY_USER_QUERY, arrayOf("id"))
            .apply {
                set(1, user.id.value)
            }
            .executeQuery()
            .iterate { getInt("id") }.let { LeafLatest.forIds(it) }
    }
}
