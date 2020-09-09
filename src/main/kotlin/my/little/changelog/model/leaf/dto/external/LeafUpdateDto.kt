package my.little.changelog.model.leaf.dto.external

import kotlinx.serialization.Serializable

@Serializable
data class LeafUpdateDto(
    val name: String,
    val valueType: Int,
    val value: String,
    val parentId: Int? = null
)