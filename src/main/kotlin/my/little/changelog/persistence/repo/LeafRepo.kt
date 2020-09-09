package my.little.changelog.persistence.repo

import my.little.changelog.model.leaf.Leaf
import my.little.changelog.persistence.AbstractCrudRepository

object LeafRepo : AbstractCrudRepository<Leaf, Int>(Leaf)
