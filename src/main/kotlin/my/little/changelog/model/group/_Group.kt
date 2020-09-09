package my.little.changelog.model.group

import my.little.changelog.model.group.dto.service.ReturnedGroupDto

fun Group.toReturnedDto(parent: ReturnedGroupDto? = null) = ReturnedGroupDto(
    id = id.value,
    vid = vid,
    name = name,
    parent = parent,
)
