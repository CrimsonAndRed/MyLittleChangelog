package my.little.changelog.model.leaf.dto.service

import kotlinx.serialization.Serializable

@Serializable
data class LeafUpdateDto(
    val id: Int,
    val name: String,
    val valueType: Int,
    val value: String,
    val parentId: Int? = null
)
