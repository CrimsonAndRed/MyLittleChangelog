package my.little.changelog.model.project

import my.little.changelog.model.auth.User
import my.little.changelog.model.auth.Users
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

class Project(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Project>(Projects)

    var name by Projects.name
    var description by Projects.description
    var user by User referencedOn Projects.user
    var order by Projects.order
}

object Projects : IntIdTable() {
    val name = text("name")
    val description = text("description")
    val user = reference("user_id", Users)
    val order = integer("order").autoIncrement("projects_order_seq")
}
