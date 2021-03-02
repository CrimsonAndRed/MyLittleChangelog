package my.little.changelog.model.diff.dto.service

import my.little.changelog.model.group.dto.service.GroupDifferenceDto
import my.little.changelog.model.leaf.dto.service.LeafDifferenceDto
import my.little.changelog.model.version.dto.service.ReturnedVersionDto

data class ReturnedDifferenceDto(
    val fromVersion: ReturnedVersionDto,
    val toVersion: ReturnedVersionDto,
    val groupContent: List<GroupDifferenceDto>,
    val leafContent: List<Pair<LeafDifferenceDto?, LeafDifferenceDto>>,
)
