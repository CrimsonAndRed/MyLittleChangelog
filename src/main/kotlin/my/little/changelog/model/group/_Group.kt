package my.little.changelog.model.group

import my.little.changelog.model.group.dto.service.ReturnedGroupDto

fun Group.toReturnedDto() = ReturnedGroupDto(
    id = id.value,
    vid = vid,
    name = name,
    parentVid = parentVid,
)
