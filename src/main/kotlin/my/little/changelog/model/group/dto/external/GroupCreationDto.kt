package my.little.changelog.model.group.dto.external

import kotlinx.serialization.Serializable

@Serializable
data class GroupCreationDto(
    val name: String,
    val parentId: Int? = null
)
