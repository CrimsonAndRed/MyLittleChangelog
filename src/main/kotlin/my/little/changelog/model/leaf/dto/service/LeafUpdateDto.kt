package my.little.changelog.model.leaf.dto.service

import my.little.changelog.configuration.auth.CustomPrincipal

data class LeafUpdateDto(
    val id: Int,
    val name: String,
    val valueType: Int,
    val value: String,
    val parentVid: Int,
    val principal: CustomPrincipal
)
