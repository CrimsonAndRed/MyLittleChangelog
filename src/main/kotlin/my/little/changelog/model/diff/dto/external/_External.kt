package my.little.changelog.model.diff.dto.external

import my.little.changelog.configuration.auth.CustomPrincipal

fun DifferenceDto.toServiceDto(cp: CustomPrincipal) = my.little.changelog.model.diff.dto.service.DifferenceDto(
    fromVersion = this.fromVersion,
    toVersion = this.toVersion,
    principal = cp
)
