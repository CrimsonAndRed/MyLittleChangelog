package my.little.changelog.model.project

import my.little.changelog.model.project.dto.service.ReturnedProjectDto

fun Project.toReturnedDto() = ReturnedProjectDto(
    id = id.value,
    order = order,
    name = name,
    description = description,
)
