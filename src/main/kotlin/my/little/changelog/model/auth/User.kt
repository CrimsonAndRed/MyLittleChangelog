package my.little.changelog.model.auth

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

class User(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<User>(Users)
    var login by Users.login
    var password by Users.password
}

object Users : IntIdTable() {
    val login = text("login")
    val password = blob("password")
}
