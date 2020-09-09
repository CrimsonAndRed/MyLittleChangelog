package my.little.changelog.persistence.repo

import my.little.changelog.model.version.Version
import my.little.changelog.persistence.AbstractCrudRepository

object VersionRepo : AbstractCrudRepository<Version, Int>(Version)
