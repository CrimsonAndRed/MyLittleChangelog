package my.little.changelog.persistence.repo

import my.little.changelog.model.group.GroupLatest
import my.little.changelog.persistence.AbstractCrudRepository

object GroupLatestRepo : AbstractCrudRepository<GroupLatest, Int>(GroupLatest)
