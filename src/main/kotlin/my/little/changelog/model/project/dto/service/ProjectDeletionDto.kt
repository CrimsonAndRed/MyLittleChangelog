package my.little.changelog.model.project.dto.service

import my.little.changelog.configuration.auth.CustomPrincipal

data class ProjectDeletionDto(
    val id: Int,
    val principal: CustomPrincipal
)
