package my.little.changelog.persistence.repo

import my.little.changelog.model.leaf.Leaf
import my.little.changelog.model.leaf.Leaves
import my.little.changelog.model.version.Version
import my.little.changelog.persistence.AbstractCrudRepository

object LeafRepo : AbstractCrudRepository<Leaf, Int>(Leaf) {

    fun findByVersion(version: Version): Iterable<Leaf> {
        return Leaf.find { Leaves.version eq version.id.value }
    }
}
