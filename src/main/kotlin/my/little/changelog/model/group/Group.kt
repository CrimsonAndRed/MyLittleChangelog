package my.little.changelog.model.group

import my.little.changelog.model.version.Version
import my.little.changelog.model.version.Versions
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

class Group(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Group>(Groups)

    var vid by Groups.vid
    var version by Version referencedOn Groups.version
    var name by Groups.name
    var parentVid by Groups.parentVid
    var order by Groups.order
}

object Groups : IntIdTable() {
    val vid = integer("vid").autoIncrement("groups_vid_seq")
    val name = text("name")
    val parentVid = integer("parent_vid").nullable()
    val version = reference("version_id", Versions)
    val order = integer("order").autoIncrement("groups_order_seq")
}
