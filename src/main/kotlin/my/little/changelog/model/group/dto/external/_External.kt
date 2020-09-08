package my.little.changelog.model.group.dto.external

fun GroupCreationDto.toServiceDto(versionId: Int) =
    my.little.changelog.model.group.dto.service.GroupCreationDto(
        name = name,
        parentId = parentId,
        versionId = versionId,
    )
