package my.little.changelog.model.diff.dto.service

import my.little.changelog.model.group.dto.service.GroupDifferenceDto
import my.little.changelog.model.leaf.dto.service.LeafDifferenceDto

data class ReturnedDifferenceDto(
    val fromVersion: Int,
    val toVersion: Int,
    val groupContent: List<GroupDifferenceDto>,
    val leafContent: List<Pair<LeafDifferenceDto?, LeafDifferenceDto>>,
)
