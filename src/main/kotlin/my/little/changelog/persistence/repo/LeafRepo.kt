package my.little.changelog.persistence.repo

import my.little.changelog.model.group.Group
import my.little.changelog.model.leaf.Leaf
import my.little.changelog.model.leaf.Leaves
import my.little.changelog.model.version.Version
import my.little.changelog.persistence.AbstractCrudRepository
import org.jetbrains.exposed.sql.SizedIterable
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.emptySized
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
        	where leaves.version_id <= ? and vid = ANY(?)
        ) sub where sub.version_id  = sub.max_version
    """

    fun findByVersion(version: Version): SizedIterable<Leaf> = transaction {
        Leaf.find { Leaves.version eq version.id.value }
            .orderBy(Leaves.order to SortOrder.ASC)
    }

    fun findCurrentGroupLeaves(group: Group): SizedIterable<Leaf> = transaction {
        Leaf.find { (Leaves.version eq group.version.id.value) and (Leaves.groupVid eq group.vid) }
            .orderBy(Leaves.order to SortOrder.ASC)
    }

    fun findCurrentGroupsLeaves(groupVids: Iterable<Int>, version: Version): SizedIterable<Leaf> = transaction {
        Leaf.find { (Leaves.version eq version.id.value) and (Leaves.groupVid inList groupVids) }
    }

    fun findDifferentialLeaves(fromVersion: Version, toVersion: Version): SizedIterable<Leaf> = transaction {
        connection.prepareStatement(DIFFERENTIAL_LEAVES_QUERY, arrayOf("id"))
            .apply {
                set(1, fromVersion.id.value)
                set(2, toVersion.id.value)
            }
            .executeQuery().iterate { getInt("id") }.let { Leaf.forIds(it) }
    }

    fun findPreDifferentialLeaves(fromVersion: Version, leaves: Iterable<Leaf>): SizedIterable<Leaf> = transaction {
        if (leaves.iterator().hasNext()) {
            connection.prepareStatement(PREDIFFERENTIAL_LEAVES_QUERY, arrayOf("id"))
                .apply {
                    set(1, fromVersion.id.value)
                    set(2, (connection.connection as java.sql.Connection).createArrayOf("INTEGER", leaves.map { it.vid }.toList().toTypedArray()))
                }
                .executeQuery().iterate { getInt("id") }.let { Leaf.forIds(it) }
        } else {
            emptySized()
        }
    }
}
