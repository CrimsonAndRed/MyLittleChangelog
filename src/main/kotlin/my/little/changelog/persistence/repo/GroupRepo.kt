package my.little.changelog.persistence.repo

import my.little.changelog.model.group.Group
import my.little.changelog.model.group.Groups
import my.little.changelog.model.leaf.Leaf
import my.little.changelog.model.version.Version
import my.little.changelog.persistence.AbstractCrudRepository
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.statements.jdbc.iterate
import org.jetbrains.exposed.sql.transactions.transaction

object GroupRepo : AbstractCrudRepository<Group, Int>(Group) {

    private const val FIND_GROUPS_AFFECTED_BY_VERSION_QUERY =
        """
        WITH RECURSIVE sublatest AS (
                SELECT grouped.* FROM (
                    SELECT g.*, max(version_id) OVER (PARTITION BY vid) FROM groups AS g
                    WHERE g.version_id <= ?
                ) as grouped
                WHERE grouped.version_id=grouped.max
            ), tmp_groups AS (
        		SELECT id, vid, parent_vid FROM sublatest 
        		WHERE (vid in (SELECT group_vid FROM leaves where version_id = ?)) or 
        				(vid in (SELECT vid FROM groups where version_id = ?))
        	UNION
        		SELECT g.id, g.vid, g.parent_vid FROM sublatest AS g JOIN tmp_groups ON g.vid=tmp_groups.parent_vid
        ) SELECT id FROM tmp_groups
        """

    private const val FIND_GROUPS_AFFECTED_BY_SUBLATEST_LEAVES_QUERY =
        """
            WITH RECURSIVE sublatest AS (
                SELECT grouped.* FROM (
                    SELECT g.*, max(version_id) OVER (PARTITION BY vid) FROM groups AS g
                    WHERE g.version_id <= ?
                ) as grouped
                WHERE grouped.version_id=grouped.max
            ),  tmp_groups AS (
                    SELECT * FROM sublatest 
                    WHERE (vid in (%s))
                UNION
                    SELECT g.* FROM sublatest AS g JOIN tmp_groups ON g.vid=tmp_groups.parent_vid
            ) SELECT id FROM tmp_groups
        """

    private const val FIND_SUBLATEST_GROUP_QUERY =
        """
            SELECT grouped.id FROM (
                SELECT g.*, max(version_id) OVER (PARTITION BY vid) FROM groups AS g
                WHERE g.vid = ? and g.version_id < ? 
            ) as grouped
            WHERE grouped.version_id=grouped.max
        """

    fun findRootGroupsByVersion(version: Version): Iterable<Group> = transaction {
        Group.find { (Groups.version eq version.id) and (Groups.parentVid eq null) }
    }

    fun findGroupsAffectedByVersion(version: Version): Iterable<Group> = transaction {
        connection.prepareStatement(FIND_GROUPS_AFFECTED_BY_VERSION_QUERY, arrayOf("id"))
            .apply {
                set(1, version.id.value)
                set(2, version.id.value)
                set(3, version.id.value)
            }
            .executeQuery().iterate { getInt("id") }.let { Group.forIds(it) }
    }

    fun findSubgroups(group: Group): Iterable<Group> = transaction {
        Group.find { (Groups.version eq group.version.id.value) and (Groups.parentVid eq group.vid) }
    }

    fun findGroupsAffectedByLeaves(leaves: Iterable<Leaf>, version: Version): Iterable<Group> = transaction {
        val groupVids: List<Int?> = leaves.map {
            it.groupVid
        }.distinct()

        if (groupVids.isEmpty()) {
            emptyList()
        } else {
            connection.prepareStatement(FIND_GROUPS_AFFECTED_BY_SUBLATEST_LEAVES_QUERY.format(groupVids.joinToString(", ")), arrayOf("id"))
                .apply {
                    set(1, version.id.value)
                }
                .executeQuery().iterate { getInt("id") }.let { Group.forIds(it) }
        }
    }

    fun findLatestGroupByVid(vid: Int): Group = transaction {
        val latestGroup = GroupLatestRepo.findByVid(vid)

        Group.find { (Groups.id eq latestGroup.id) }.single()
    }

    fun findSublatestGroup(vid: Int, versionId: Int): Group? = transaction {
        connection.prepareStatement(FIND_SUBLATEST_GROUP_QUERY, arrayOf("id"))
            .apply {
                set(1, vid)
                set(2, versionId)
            }
            .executeQuery()
            .let {
                if (it.next()) {
                    val id = it.getInt("id")
                    Group.findById(id)
                } else {
                    null
                }
            }
    }
}
