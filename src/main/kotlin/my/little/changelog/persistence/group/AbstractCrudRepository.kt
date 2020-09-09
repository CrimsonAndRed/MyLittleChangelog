package my.little.changelog.persistence.group

import my.little.changelog.model.RepoCreationDto
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.sql.transactions.transaction

abstract class AbstractCrudRepository<E : Entity<ID>, ID : Comparable<ID>>(
    private val accessor: EntityClass<ID, E>
) : CrudRepository<E, ID> {
    override fun findAll(): Iterable<E> = transaction { accessor.all() }

    override fun findById(id: ID): E = transaction { accessor[id] }

    override fun update(entity: E): E = transaction { entity.apply { flush() } }

    override fun create(dto: RepoCreationDto<E>?): E = transaction { accessor.new(dto?.convertToEntity() ?: {}) }

    override fun delete(entity: E) = transaction { entity.delete() }

    override fun deleteById(id: ID) = transaction { findById(id).delete() }
}
