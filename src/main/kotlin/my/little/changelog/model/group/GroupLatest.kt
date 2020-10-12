package my.little.changelog.model.group

import my.little.changelog.model.version.Version
import my.little.changelog.model.version.Versions
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

class GroupLatest(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<GroupLatest>(GroupsLatest)

    var vid by GroupsLatest.vid
    var version by Version referencedOn GroupsLatest.version
    var name by GroupsLatest.name
    var parentVid by GroupsLatest.parentVid
}

object GroupsLatest : IntIdTable("groups_latest") {
    val vid = integer("vid").autoIncrement("groups_vid_seq")
    val name = text("name")
    val parentVid = integer("parent_vid").nullable()
    val version = reference("version_id", Versions)
}
