package my.little.changelog.persistence.repo

import my.little.changelog.model.group.Group
import my.little.changelog.model.leaf.Leaf
import my.little.changelog.model.leaf.Leaves
import my.little.changelog.model.version.Version
import my.little.changelog.persistence.AbstractCrudRepository
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.statements.jdbc.iterate
import org.jetbrains.exposed.sql.transactions.transaction

object LeafRepo : AbstractCrudRepository<Leaf, Int>(Leaf) {

    private const val DIFFERENTIAL_LEAVES_QUERY =
        """
        select id from (
        	select *, max(leaves.version_id) over (partition by leaves.vid) max_version
        	from leaves
        	where leaves.version_id > ? and leaves.version_id <= ?
        ) sub where sub.version_id  = sub.max_version
    """

    private const val PREDIFFERENTIAL_LEAVES_QUERY =
        """
        select id from (
        	select *, max(leaves.version_id) over (partition by leaves.vid) max_version
        	from leaves
        	where leaves.version_id <= ? and vid in (%s)
        ) sub where sub.version_id  = sub.max_version
    """

    fun findByVersion(version: Version): Iterable<Leaf> = transaction {
        Leaf.find { Leaves.version eq version.id.value }
    }

    fun findCurrentGroupLeaves(group: Group): Iterable<Leaf> = transaction {
        Leaf.find { (Leaves.version eq group.version.id.value) and (Leaves.groupVid eq group.vid) }
    }

    fun findDifferentialLeaves(fromVersion: Version, toVersion: Version): Iterable<Leaf> = transaction {
        connection.prepareStatement(DIFFERENTIAL_LEAVES_QUERY, arrayOf("id"))
            .apply {
                set(1, fromVersion.id.value)
                set(2, toVersion.id.value)
            }
            .executeQuery().iterate { getInt("id") }.let { Leaf.forIds(it) }
    }

    fun findPreDifferentialLeaves(fromVersion: Version, leaves: Iterable<Leaf>): Iterable<Leaf> = transaction {
        if (leaves.iterator().hasNext()) {
            connection.prepareStatement(PREDIFFERENTIAL_LEAVES_QUERY.format(leaves.map { it.vid }.joinToString(", ")), arrayOf("id"))
                .apply {
                    set(1, fromVersion.id.value)
                }
                .executeQuery().iterate { getInt("id") }.let { Leaf.forIds(it) }
        } else {
            emptyList()
        }
    }
}
