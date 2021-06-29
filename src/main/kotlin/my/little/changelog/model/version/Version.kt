package my.little.changelog.model.version

import my.little.changelog.model.auth.User
import my.little.changelog.model.auth.Users
import my.little.changelog.model.project.Project
import my.little.changelog.model.project.Projects
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

class Version(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Version>(Versions)

    var name by Versions.name
    var order by Versions.order
    var user by User referencedOn Versions.user
    var project by Project referencedOn Versions.project
}

object Versions : IntIdTable() {
    val name = text("name")
    val order = integer("order").autoIncrement("versions_order_seq")
    val user = reference("user_id", Users)
    val project = reference("project_id", Projects)
}
