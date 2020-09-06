package my.little.changelog.persistence.group

import my.little.changelog.model.group.Group
import my.little.changelog.model.version.Version
import org.jetbrains.exposed.sql.statements.jdbc.iterate
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

object GroupsRepo {

    private const val FIND_GROUPS_AFFECTED_BY_VERSION_QUERY = """
        WITH RECURSIVE tmp_groups AS (
        		SELECT * FROM groups_latest 
        		WHERE (vid in (SELECT group_vid FROM leaves where version_id = ?)) or 
        				(vid in (SELECT vid FROM groups where version_id = ?))
        	UNION
        		SELECT g.* FROM groups_latest AS g JOIN tmp_groups ON g.vid=tmp_groups.parent_vid
        ) SELECT id FROM tmp_groups;
    """

    // TODO транзакции срут на лицо
    suspend fun findGroupAffectedByVersion(version: Version): Iterable<Group> = newSuspendedTransaction {
        connection.prepareStatement(FIND_GROUPS_AFFECTED_BY_VERSION_QUERY, arrayOf("id"))
            .apply {
                set(1, version.id.value)
                set(2, version.id.value)
            }
            .executeQuery().iterate { getInt("id") }.let { Group.forIds(it) }
            .also { Version.new {  } }
    }
}