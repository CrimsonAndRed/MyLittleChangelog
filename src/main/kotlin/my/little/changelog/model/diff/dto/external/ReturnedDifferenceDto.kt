package my.little.changelog.model.diff.dto.external

import kotlinx.serialization.Serializable
import my.little.changelog.model.group.dto.external.GroupDifferenceDto
import my.little.changelog.model.leaf.dto.external.LeafDifferenceDto

@Serializable
data class ReturnedDifferenceDto(
    val from: Int,
    val to: Int,
    val groupContent: List<GroupDifferenceDto>,
    val leafContent: List<LeafDifferenceDto>,
)
