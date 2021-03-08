package my.little.changelog.model.group.dto.external

import my.little.changelog.configuration.auth.CustomPrincipal

fun GroupCreationDto.toServiceDto(versionId: Int, cp: CustomPrincipal) = my.little.changelog.model.group.dto.service.GroupCreationDto(
    name = name!!,
    vid = vid,
    parentVid = parentVid,
    versionId = versionId,
    principal = cp
)

fun GroupUpdateDto.toServiceDto(groupId: Int, cp: CustomPrincipal) = my.little.changelog.model.group.dto.service.GroupUpdateDto(
    name = name!!,
    parentVid = parentVid,
    id = groupId,
    principal = cp
)
