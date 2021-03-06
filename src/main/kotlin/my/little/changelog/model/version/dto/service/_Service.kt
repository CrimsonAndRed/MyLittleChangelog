package my.little.changelog.model.version.dto.service

import my.little.changelog.model.auth.User
import my.little.changelog.model.project.Project

fun ReturnedVersionDto.toExternalDto() = my.little.changelog.model.version.dto.external.ReturnedVersionDto(
    id = id,
    name = name,
    order = order
)

fun VersionCreationDto.toRepoDto(user: User, project: Project) = my.little.changelog.model.version.dto.repo.VersionCreationDto(
    name = name,
    user = user,
    project = project
)
