package my.little.changelog.model.group.dto.service

import my.little.changelog.model.group.Group
import my.little.changelog.model.version.Version

fun GroupCreationDto.toRepoDto(version: Version, parent: Group?) = my.little.changelog.model.group.dto.repo.GroupCreationDto(
    name = name,
    vid = vid,
    parentVid = parent?.vid,
    version = version,
)
