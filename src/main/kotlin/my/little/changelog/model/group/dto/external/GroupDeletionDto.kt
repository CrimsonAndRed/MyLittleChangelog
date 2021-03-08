package my.little.changelog.model.group.dto.external

import my.little.changelog.configuration.auth.CustomPrincipal

data class GroupDeletionDto(
    val id: Int,
    val principal: CustomPrincipal
)
