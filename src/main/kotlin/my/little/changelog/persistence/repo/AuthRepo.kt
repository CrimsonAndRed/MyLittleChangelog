package my.little.changelog.persistence.repo

import my.little.changelog.model.auth.User
import my.little.changelog.model.auth.Users
import my.little.changelog.persistence.AbstractCrudRepository

object AuthRepo : AbstractCrudRepository<User, Int>(User) {

    fun countByLogin(login: String): Long {
        return User.find { Users.login eq login }.count()
    }
}
