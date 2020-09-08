package my.little.changelog.model.leaf.dto.external

fun LeafCreationDto.toServiceDto(groupId: Int, versionId: Int) =
    my.little.changelog.model.leaf.dto.service.LeafCreationDto(
        name = name,
        valueType = valueType,
        value = value,
        groupId = groupId,
        versionId = versionId,
    )
