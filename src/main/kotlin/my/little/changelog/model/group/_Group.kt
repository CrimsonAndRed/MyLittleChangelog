package my.little.changelog.model.group

import my.little.changelog.model.group.dto.GroupDto
import my.little.changelog.model.leaf.dto.LeafDto

fun Group.toDto(groupContent: List<GroupDto>, leafContent: List<LeafDto>) = GroupDto(
    id = id.value,
    vid = vid,
    version = version.id.value,
    name = name,
    groupContent = groupContent,
    leafContent = leafContent,
)