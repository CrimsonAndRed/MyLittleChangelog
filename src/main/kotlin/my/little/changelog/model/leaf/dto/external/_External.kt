package my.little.changelog.model.leaf.dto.external

fun LeafCreationDto.toServiceDto(groupId: Int, versionId: Int) =
    my.little.changelog.model.leaf.dto.service.LeafCreationDto(
        vid = vid,
        name = name,
        valueType = valueType,
        value = value,
        groupId = groupId,
        versionId = versionId,
    )

fun LeafUpdateDto.toServiceDto(leafId: Int) = my.little.changelog.model.leaf.dto.service.LeafUpdateDto(
    id = leafId,
    name = name,
    valueType = valueType,
    value = value,
    parentVid = parentVid,
)

fun LeafDeletionDto.toServiceDto() = my.little.changelog.model.leaf.dto.service.LeafDeletionDto(
    id = id
)
