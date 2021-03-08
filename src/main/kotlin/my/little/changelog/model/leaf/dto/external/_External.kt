package my.little.changelog.model.leaf.dto.external

import my.little.changelog.configuration.auth.CustomPrincipal

fun LeafCreationDto.toServiceDto(groupId: Int, versionId: Int, cp: CustomPrincipal) =
    my.little.changelog.model.leaf.dto.service.LeafCreationDto(
        vid = vid,
        name = name!!,
        valueType = valueType!!,
        value = value!!,
        groupId = groupId,
        versionId = versionId,
        principal = cp
    )

fun LeafUpdateDto.toServiceDto(leafId: Int, cp: CustomPrincipal) = my.little.changelog.model.leaf.dto.service.LeafUpdateDto(
    id = leafId,
    name = name!!,
    valueType = valueType!!,
    value = value!!,
    parentVid = parentVid,
    principal = cp
)

fun LeafDeletionDto.toServiceDto(cp: CustomPrincipal) = my.little.changelog.model.leaf.dto.service.LeafDeletionDto(
    id = id,
    principal = cp
)
