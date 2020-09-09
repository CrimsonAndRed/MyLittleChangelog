package my.little.changelog.persistence.group

import my.little.changelog.model.leaf.Leaf
import my.little.changelog.model.leaf.dto.repo.LeafCreationDto
import org.jetbrains.exposed.sql.transactions.transaction

object LeafRepo {

    fun findLeafById(id: Int): Leaf  = transaction {
        Leaf[id]
    }

    fun createLeaf(leaf: LeafCreationDto): Leaf = transaction {
        Leaf.new {
            name = leaf.name
            valueType = leaf.valueType
            value = leaf.value
            version = leaf.version
            groupVid = leaf.group.vid
        }
    }


    fun updateLeaf(leaf: Leaf): Leaf = transaction {
        leaf.apply { flush() }
    }
}
