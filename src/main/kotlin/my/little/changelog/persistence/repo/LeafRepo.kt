package my.little.changelog.persistence.repo

import my.little.changelog.model.group.Group
import my.little.changelog.model.leaf.Leaf
import my.little.changelog.model.leaf.Leaves
import my.little.changelog.model.version.Version
import my.little.changelog.persistence.AbstractCrudRepository
import org.jetbrains.exposed.sql.and

object LeafRepo : AbstractCrudRepository<Leaf, Int>(Leaf) {

    fun findByVersion(version: Version): Iterable<Leaf> {
        return Leaf.find { Leaves.version eq version.id.value }
    }

    fun findCurrentGroupLeaves(group: Group): Iterable<Leaf> {
        return Leaf.find { (Leaves.version eq group.version.id.value) and (Leaves.groupVid eq group.vid) }
    }
}
