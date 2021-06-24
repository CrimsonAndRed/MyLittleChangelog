package my.little.changelog.model.group.dto.service

import my.little.changelog.configuration.auth.CustomPrincipal

data class GroupUpdateDto(
    val id: Int,
    val name: String,
    val parentVid: Int? = null,
    val principal: CustomPrincipal
)
