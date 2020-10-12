package my.little.changelog.model.leaf.dto.service

import my.little.changelog.model.group.Group
import my.little.changelog.model.version.Version

fun LeafCreationDto.toRepoDto(version: Version, group: Group) =
    my.little.changelog.model.leaf.dto.repo.LeafCreationDto(
        vid = vid,
        name = name,
        valueType = valueType,
        value = value,
        group = group,
        version = version,
    )

fun LeafReturnedDto.toExternalDto() = my.little.changelog.model.leaf.dto.external.LeafReturnedDto(
    id = id,
    vid = vid,
    name = name,
    valueType = valueType,
    value = value,
)

fun Pair<LeafDifferenceDto?, LeafDifferenceDto>.toExternalDto() = my.little.changelog.model.leaf.dto.external.LeafDifferenceDto(
    id = second.id,
    vid = second.vid,
    name = second.name,
    valueType = second.valueType,
    valueDiff = "${first?.value ?: ""} -> ${second.value}"
)
