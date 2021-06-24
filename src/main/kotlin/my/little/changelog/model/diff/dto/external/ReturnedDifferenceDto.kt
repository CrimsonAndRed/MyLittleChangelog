package my.little.changelog.model.diff.dto.external

import kotlinx.serialization.Serializable
import my.little.changelog.model.group.dto.external.GroupDifferenceDto
import my.little.changelog.model.leaf.dto.external.LeafDifferenceDto
import my.little.changelog.model.version.dto.external.ReturnedVersionDto

@Serializable
data class ReturnedDifferenceDto(
    val from: ReturnedVersionDto,
    val to: ReturnedVersionDto,
    val groupContent: List<GroupDifferenceDto>,
    val leafContent: List<LeafDifferenceDto>,
)
