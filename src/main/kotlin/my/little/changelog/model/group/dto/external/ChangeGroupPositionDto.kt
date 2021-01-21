package my.little.changelog.model.group.dto.external

import kotlinx.serialization.Serializable

@Serializable
data class ChangeGroupPositionDto(
    val changeAgainstId: Int
)
