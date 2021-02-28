package my.little.changelog.model.group.dto.service

import my.little.changelog.model.leaf.dto.service.toExternalDto
import my.little.changelog.model.version.Version

fun GroupCreationDto.toRepoDto(version: Version) = my.little.changelog.model.group.dto.repo.GroupCreationDto(
    name = name,
    vid = vid,
    parentVid = parentVid,
    version = version,
    order = order
)

fun ReturnedGroupDto.toExternalDto() = my.little.changelog.model.group.dto.external.ReturnedGroupDto(
    id = id,
    vid = vid,
    name = name,
    parentVid = parentVid,
)

fun GroupDifferenceDto.toExternalDto(): my.little.changelog.model.group.dto.external.GroupDifferenceDto = my.little.changelog.model.group.dto.external.GroupDifferenceDto(
    id = id,
    vid = vid,
    name = name,
    groupContent = this.groupContent.map { it.toExternalDto() },
    leafContent = this.leafContent.map { it.toExternalDto() }
)
