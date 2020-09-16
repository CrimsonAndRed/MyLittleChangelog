package my.little.changelog.persistence.repo

import my.little.changelog.model.group.Group
import my.little.changelog.model.group.Groups
import my.little.changelog.model.version.Version
import my.little.changelog.persistence.AbstractCrudRepository
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.statements.jdbc.iterate
import org.jetbrains.exposed.sql.transactions.transaction

object GroupRepo : AbstractCrudRepository<Group, Int>(Group) {

    private const val FIND_GROUPS_AFFECTED_BY_VERSION_QUERY =
        """
        WITH RECURSIVE tmp_groups AS (
        		SELECT * FROM groups_latest 
        		WHERE (vid in (SELECT group_vid FROM leaves where version_id = ?)) or 
        				(vid in (SELECT vid FROM groups where version_id = ?))
        	UNION
        		SELECT g.* FROM groups_latest AS g JOIN tmp_groups ON g.vid=tmp_groups.parent_vid
        ) SELECT id FROM tmp_groups;
    """

    fun findGroupAffectedByVersion(version: Version): Iterable<Group> = transaction {
        connection.prepareStatement(FIND_GROUPS_AFFECTED_BY_VERSION_QUERY, arrayOf("id"))
            .apply {
                set(1, version.id.value)
                set(2, version.id.value)
            }
            .executeQuery().iterate { getInt("id") }.let { Group.forIds(it) }
    }

    fun findSubgroups(group: Group): Iterable<Group> = transaction {
        Group.find { (Groups.version eq group.version.id.value) and (Groups.parentVid eq group.vid) }
    }
}
