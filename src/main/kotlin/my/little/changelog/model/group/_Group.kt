package my.little.changelog.model.group

import my.little.changelog.model.group.dto.external.GroupDto
import my.little.changelog.model.group.dto.external.ReturnedGroupDto
import my.little.changelog.model.leaf.dto.external.LeafDto

fun Group.toDto(groupContent: List<GroupDto>, leafContent: List<LeafDto>) = GroupDto(
    id = id.value,
    vid = vid,
    version = version.id.value,
    name = name,
    groupContent = groupContent,
    leafContent = leafContent,
)

fun Group.toReturnedGroupDto(parentId: Int?) = ReturnedGroupDto(
    id = id.value,
    vid = vid,
    name = name,
    parentId = parentId,
)
