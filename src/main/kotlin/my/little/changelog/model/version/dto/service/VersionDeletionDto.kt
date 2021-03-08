package my.little.changelog.model.version.dto.service

import my.little.changelog.configuration.auth.CustomPrincipal

data class VersionDeletionDto(
    val id: Int,
    val principal: CustomPrincipal
)
