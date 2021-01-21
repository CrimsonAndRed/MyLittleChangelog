package my.little.changelog.model.leaf.dto.external

import kotlinx.serialization.Serializable

@Serializable
data class ChangeLeafPositionDto(
    val changeAgainstId: Int
)
