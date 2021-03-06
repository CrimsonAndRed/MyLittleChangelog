package my.little.changelog.persistence.repo

import my.little.changelog.model.auth.User
import my.little.changelog.persistence.AbstractCrudRepository

object AuthRepo : AbstractCrudRepository<User, Int>(User)
