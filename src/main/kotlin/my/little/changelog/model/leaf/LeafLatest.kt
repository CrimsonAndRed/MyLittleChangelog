package my.little.changelog.model.leaf

import my.little.changelog.model.version.Version
import my.little.changelog.model.version.Versions
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

class LeafLatest(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<LeafLatest>(LeavesLatest)

    var vid by LeavesLatest.vid
    var name by LeavesLatest.name
    var valueType by LeavesLatest.valueType
    var value by LeavesLatest.value
    var version by Version referencedOn LeavesLatest.version
    var groupVid by LeavesLatest.groupVid
    var order by LeavesLatest.order
    var isDeleted by LeavesLatest.isDeleted
}

object LeavesLatest : IntIdTable("leaves_latest") {
    val vid = integer("vid").autoIncrement("leaves_vid_seq")
    val name = text("name")
    val valueType = integer("value_type")
    val value = text("value")
    val version = reference("version_id", Versions)
    val groupVid = integer("group_vid").nullable()
    val order = integer("order").autoIncrement("leaves_order_seq")
    val isDeleted = bool("is_deleted")
}
