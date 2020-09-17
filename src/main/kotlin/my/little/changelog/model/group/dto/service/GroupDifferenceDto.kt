package my.little.changelog.model.group.dto.service

import my.little.changelog.model.leaf.dto.service.LeafDifferenceDto

data class GroupDifferenceDto(
    val id: Int,
    val vid: Int,
    val name: String,
    val groupContent: List<GroupDifferenceDto>,
    val leafContent: List<Pair<LeafDifferenceDto?, LeafDifferenceDto>>,
)
