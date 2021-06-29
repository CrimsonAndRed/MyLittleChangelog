package my.little.changelog.model.project.dto.service

import my.little.changelog.model.auth.User

fun ReturnedProjectDto.toExternalDto() = my.little.changelog.model.project.dto.external.ReturnedProjectDto(
    id = id,
    name = name,
    description = description,
    order = order
)

fun ProjectCreationDto.toRepoDto(user: User) = my.little.changelog.model.project.dto.repo.ProjectCreationDto(
    name = name,
    description = description,
    user = user
)
