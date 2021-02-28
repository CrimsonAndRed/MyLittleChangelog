package my.little.changelog.model.version

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

class Version(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Version>(Versions)

    var name by Versions.name
}

object Versions : IntIdTable() {
    val name = text("name")
}
