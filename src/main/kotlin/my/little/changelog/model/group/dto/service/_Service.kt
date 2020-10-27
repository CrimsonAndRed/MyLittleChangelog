package my.little.changelog.model.group.dto.service

import my.little.changelog.model.group.Group
import my.little.changelog.model.leaf.dto.service.toExternalDto
import my.little.changelog.model.version.Version

fun GroupCreationDto.toRepoDto(version: Version, parent: Group?) = my.little.changelog.model.group.dto.repo.GroupCreationDto(
    name = name,
    vid = vid,
    parentVid = parent?.vid,
    version = version,
)

fun ReturnedGroupDto.toExternalDto() = my.little.changelog.model.group.dto.external.ReturnedGroupDto(
    id = id,
    vid = vid,
    name = name,
    parentId = parentId,
)

fun GroupDifferenceDto.toExternalDto(): my.little.changelog.model.group.dto.external.GroupDifferenceDto = my.little.changelog.model.group.dto.external.GroupDifferenceDto(
    id = id,
    vid = vid,
    name = name,
    groupContent = this.groupContent.map { it.toExternalDto() },
    leafContent = this.leafContent.map { it.toExternalDto() }
)
