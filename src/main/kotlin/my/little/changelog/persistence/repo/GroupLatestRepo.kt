package my.little.changelog.persistence.repo

import my.little.changelog.model.auth.User
import my.little.changelog.model.group.GroupLatest
import my.little.changelog.model.group.GroupsLatest
import my.little.changelog.persistence.AbstractCrudRepository
import org.jetbrains.exposed.sql.SizedIterable
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

    private const val FIND_GROUPS_BY_USER_QUERY =
        """
           SELECT groups_latest.id FROM groups_latest
           JOIN versions ON groups_latest.version_id = versions.id
           WHERE versions.user_id = ? AND groups_latest.is_deleted <> true
        """

    fun findByVid(vid: Int): GroupLatest = transaction {
        GroupLatest.find { GroupsLatest.vid eq vid }.single()
    }

    fun findHierarchyToChildByVid(vid: Int): SizedIterable<GroupLatest> = transaction {
        raw(FIND_GROUPS_AFFECTED_BY_GROUP_QUERY, arrayOf("id")) {
            set(1, vid)
        }.iterate { getInt("id") }.let { GroupLatest.forIds(it) }
    }

    fun findParentIds(vid: Int): List<Int> = transaction {
        raw(FIND_PARENT_GROUPS_QUERY, arrayOf("id")) {
            set(1, vid)
        }.iterate { getInt("id") }
    }

    fun findAllByUserNotDeleted(user: User): SizedIterable<GroupLatest> = transaction {
        raw(FIND_GROUPS_BY_USER_QUERY, arrayOf("id")) {
            set(1, user.id.value)
        }.iterate { getInt("id") }.let { GroupLatest.forIds(it) }
    }
}
