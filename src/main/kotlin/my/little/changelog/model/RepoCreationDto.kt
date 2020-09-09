package my.little.changelog.model

import org.jetbrains.exposed.dao.Entity

interface RepoCreationDto<E : Entity<*>> {
    fun convertToEntity(): E.() -> Unit
}
