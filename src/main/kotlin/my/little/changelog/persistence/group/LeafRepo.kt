package my.little.changelog.persistence.group

import my.little.changelog.model.leaf.Leaf

object LeafRepo : AbstractCrudRepository<Leaf, Int>(Leaf)
