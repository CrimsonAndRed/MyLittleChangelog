package my.little.changelog.model.leaf.dto

import kotlinx.serialization.Serializable

@Serializable
data class LeafDto(
    val id: Int,
    val vid: Int,
    val name: String,
    val valueType: Int,
    val value: String,
)
