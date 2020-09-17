package my.little.changelog.model.leaf.dto.service

import my.little.changelog.model.group.Group
import my.little.changelog.model.version.Version

fun LeafCreationDto.toRepoDto(version: Version, group: Group) =
    my.little.changelog.model.leaf.dto.repo.LeafCreationDto(
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

fun Pair<LeafDifferenceDto?, LeafDifferenceDto>.toExternalDto(): my.little.changelog.model.leaf.dto.external.LeafDifferenceDto {
    val oldLeafValue = if (this.first == null) {
        ""
    } else {
        this.first?.value
    }
    return my.little.changelog.model.leaf.dto.external.LeafDifferenceDto(
        id = this.second.id,
        vid = this.second.vid,
        name = this.second.name,
        valueType = this.second.valueType,
        valueDiff = "${oldLeafValue} -> ${this.second.value}"
    )
}