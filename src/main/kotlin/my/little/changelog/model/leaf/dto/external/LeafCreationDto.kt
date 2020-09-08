package my.little.changelog.model.leaf.dto.external

import kotlinx.serialization.Serializable

@Serializable
data class LeafCreationDto(
    val name: String,
    val valueType: Int,
    val value: String,
)