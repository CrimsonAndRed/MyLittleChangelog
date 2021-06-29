package my.little.changelog.model.version.dto.service

import my.little.changelog.configuration.auth.CustomPrincipal

data class VersionCreationDto(
    val name: String,
    val projectId: Int,
    val principal: CustomPrincipal
)
