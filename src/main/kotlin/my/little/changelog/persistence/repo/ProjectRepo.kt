package my.little.changelog.persistence.repo

import my.little.changelog.model.auth.User
import my.little.changelog.model.project.Project
import my.little.changelog.model.project.Projects
import my.little.changelog.persistence.AbstractCrudRepository
import org.jetbrains.exposed.sql.SizedIterable
import org.jetbrains.exposed.sql.transactions.transaction

object ProjectRepo : AbstractCrudRepository<Project, Int>(Project) {

    fun findByUser(user: User): SizedIterable<Project> = transaction {
        Project.find { Projects.user eq user.id }
    }
}
