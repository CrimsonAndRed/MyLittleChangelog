package my.little.changelog.model.project.dto.service

import my.little.changelog.configuration.auth.CustomPrincipal

data class ProjectCreationDto(
    val name: String,
    val description: String,
    val principal: CustomPrincipal
)
