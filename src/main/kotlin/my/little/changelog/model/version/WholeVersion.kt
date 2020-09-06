package my.little.changelog.model.version

import kotlinx.serialization.Serializable
import my.little.changelog.model.group.dto.GroupDto
import my.little.changelog.model.leaf.dto.LeafDto

@Serializable
data class WholeVersion(
    val id: Int,
    val groupContent: List<GroupDto>,
    val leafContent: List<LeafDto>,
)