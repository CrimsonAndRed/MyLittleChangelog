package my.little.changelog.model.project.dto.external

import my.little.changelog.configuration.auth.CustomPrincipal
import my.little.changelog.model.project.Project
import my.little.changelog.model.project.Projects
import org.jetbrains.exposed.dao.id.EntityID

fun ReturnedProjectDto.toModel() = Project(
    id = EntityID(id, Projects)
)

fun ProjectCreationDto.toServiceDto(cp: CustomPrincipal) = my.little.changelog.model.project.dto.service.ProjectCreationDto(
    name = name!!,
    description = description!!,
    principal = cp
)
