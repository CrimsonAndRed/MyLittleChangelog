package my.little.changelog.model.version.dto

import my.little.changelog.model.version.Version
import my.little.changelog.model.version.Versions
import org.jetbrains.exposed.dao.id.EntityID

fun VersionDto.toModel() = Version(
    id = EntityID(id, Versions)
)
