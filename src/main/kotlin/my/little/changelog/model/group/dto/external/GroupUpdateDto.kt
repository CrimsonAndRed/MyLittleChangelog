package my.little.changelog.model.group.dto.external

import kotlinx.serialization.Serializable

@Serializable
data class GroupUpdateDto(
    val name: String,
    val parentVid: Int? = null
)
