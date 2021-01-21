package my.little.changelog.persistence.repo

import my.little.changelog.model.leaf.LeafLatest
import my.little.changelog.persistence.AbstractCrudRepository

object LeafLatestRepo : AbstractCrudRepository<LeafLatest, Int>(LeafLatest)
