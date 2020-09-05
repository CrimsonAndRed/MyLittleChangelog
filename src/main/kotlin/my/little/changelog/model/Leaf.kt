package my.little.changelog.model

import my.little.changelog.model.version.Version
import my.little.changelog.model.version.Versions
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

class Leaf(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Leaf>(Leaves)

    var vid by Leaves.vid
    var name by Leaves.name
    var valueType by Leaves.valueType
    var value by Leaves.value
    var version by Version referencedOn Leaves.version
    var groupVid by Leaves.groupVid
}

object Leaves : IntIdTable() {
    val vid = integer("vid")
    val name = text("name")
    val valueType = integer("value_type")
    val value = text("value")
    val version = reference("version_id", Versions)
    val groupVid = integer("group_vid")
}
