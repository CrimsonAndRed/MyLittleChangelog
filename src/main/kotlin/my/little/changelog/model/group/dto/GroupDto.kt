package my.little.changelog.model.group.dto

import kotlinx.serialization.Serializable
import my.little.changelog.model.leaf.dto.external.LeafDto

@Serializable
data class GroupDto(
    val id: Int,
    val vid: Int,
    val version: Int,
    val name: String,
    val groupContent: List<GroupDto>,
    val leafContent: List<LeafDto>,
)