package my.little.changelog.model.group.dto.external

import kotlinx.serialization.Serializable

@Serializable
class ReturnedGroupDto(
    val id: Int,
    val vid: Int,
    val name: String,
    val parentId: Int? = null
)
