package my.little.changelog.model.group.dto.service

import my.little.changelog.configuration.auth.CustomPrincipal

data class GroupCreationDto(
    val name: String,
    val vid: Int? = null,
    val parentVid: Int? = null,
    val versionId: Int,
    val order: Int? = null,
    val principal: CustomPrincipal
)
