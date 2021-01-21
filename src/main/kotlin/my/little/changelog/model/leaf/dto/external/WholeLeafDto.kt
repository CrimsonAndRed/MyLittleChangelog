package my.little.changelog.model.leaf.dto.external

import kotlinx.serialization.Serializable

@Serializable
data class WholeLeafDto(
    val id: Int,
    val vid: Int,
    val name: String,
    val valueType: Int,
    val value: String,
)
