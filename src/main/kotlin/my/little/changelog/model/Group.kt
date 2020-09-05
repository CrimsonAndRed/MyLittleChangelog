package my.little.changelog.model

import my.little.changelog.model.version.Version
import my.little.changelog.model.version.Versions
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

class Group(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Group>(Groups)

    var vid by Groups.vid
    val version by Version referencedOn Groups.version
    var name by Groups.name
    var parentVid by Groups.parentVid
}

object Groups : IntIdTable() {
    val vid = integer("vid")
    val name = text("name")
    val parentVid = integer("parent_vid")
    val version = reference("version_id", Versions)
}
