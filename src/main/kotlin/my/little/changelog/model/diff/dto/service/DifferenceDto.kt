package my.little.changelog.model.diff.dto.service

import my.little.changelog.configuration.auth.CustomPrincipal

data class DifferenceDto(
    val fromVersion: Int,
    val toVersion: Int,
    val principal: CustomPrincipal
)
