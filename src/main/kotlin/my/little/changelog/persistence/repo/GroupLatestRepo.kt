package my.little.changelog.persistence.repo

import my.little.changelog.model.group.GroupLatest
import my.little.changelog.model.group.GroupsLatest
import my.little.changelog.persistence.AbstractCrudRepository
import org.jetbrains.exposed.sql.statements.jdbc.iterate
import org.jetbrains.exposed.sql.transactions.transaction

object GroupLatestRepo : AbstractCrudRepository<GroupLatest, Int>(GroupLatest) {

    private const val FIND_GROUPS_AFFECTED_BY_GROUP_QUERY =
        """
            WITH RECURSIVE tmp_groups AS (
                    SELECT * FROM groups_latest 
                    WHERE vid = ?
                UNION
                    SELECT g.* FROM groups_latest AS g JOIN tmp_groups ON g.parent_vid=tmp_groups.vid
            ) SELECT id FROM tmp_groups
        """

    private const val FIND_PARENT_GROUPS_QUERY =
        """
            WITH RECURSIVE parents AS (
                    SELECT id, parent_vid FROM groups_latest
                    WHERE vid = ?
                UNION
                    SELECT g.id, g.parent_vid FROM groups_latest AS g JOIN parents ON g.vid=parents.parent_vid
            ) SELECT id FROM parents
        """

    fun findByVid(vid: Int): GroupLatest = transaction {
        GroupLatest.find { GroupsLatest.vid eq vid }.single()
    }

    fun findHierarchyToChildByVid(vid: Int): Iterable<GroupLatest> = transaction {
        connection.prepareStatement(FIND_GROUPS_AFFECTED_BY_GROUP_QUERY, arrayOf("id"))
            .apply {
                set(1, vid)
            }
            .executeQuery().iterate { getInt("id") }.let { GroupLatest.forIds(it) }
    }

    fun findParentIds(vid: Int?): Iterable<Int> = transaction {
        connection.prepareStatement(FIND_PARENT_GROUPS_QUERY, arrayOf("id"))
            .apply {
                set(1, vid)
            }
            .executeQuery()
            .iterate { getInt("id") }
    }
}
