package my.little.changelog.model.leaf.dto.service

import my.little.changelog.configuration.auth.CustomPrincipal

class LeafDeletionDto(
    val id: Int,
    val principal: CustomPrincipal
)
