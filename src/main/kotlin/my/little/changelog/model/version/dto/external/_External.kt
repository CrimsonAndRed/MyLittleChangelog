package my.little.changelog.model.version.dto.external

import my.little.changelog.configuration.auth.CustomPrincipal
import my.little.changelog.model.version.Version
import my.little.changelog.model.version.Versions
import org.jetbrains.exposed.dao.id.EntityID

fun ReturnedVersionDto.toModel() = Version(
    id = EntityID(id, Versions)
)

fun VersionCreationDto.toServiceDto(projectId: Int, cp: CustomPrincipal) = my.little.changelog.model.version.dto.service.VersionCreationDto(
    name = name,
    projectId = projectId,
    principal = cp
)
