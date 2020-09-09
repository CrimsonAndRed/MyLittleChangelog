package my.little.changelog.model.leaf.dto.external

fun LeafCreationDto.toServiceDto(groupId: Int, versionId: Int) =
    my.little.changelog.model.leaf.dto.service.LeafCreationDto(
        name = name,
        valueType = valueType,
        value = value,
        groupId = groupId,
        versionId = versionId,
    )

fun LeafUpdateDto.toServiceDto(leafId: Int) =
    my.little.changelog.model.leaf.dto.service.LeafUpdateDto(
        id = leafId,
        name = name,
        valueType = valueType,
        value = value,
        parentId = parentId,
    )
