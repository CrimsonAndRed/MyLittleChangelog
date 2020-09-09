package my.little.changelog.persistence.group

import my.little.changelog.model.version.Version

object VersionRepo : AbstractCrudRepository<Version, Int>(Version)
