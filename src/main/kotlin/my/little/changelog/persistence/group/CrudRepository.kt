package my.little.changelog.persistence.group

import my.little.changelog.model.RepoCreationDto
import org.jetbrains.exposed.dao.Entity

interface CrudRepository<E : Entity<ID>, ID : Comparable<ID>> {
    fun findAll(): Iterable<E>
    fun findById(id: ID): E
    fun update(entity: E): E
    fun create(dto: RepoCreationDto<E>? = null): E
    fun delete(entity: E)
    fun deleteById(id: ID)
}
