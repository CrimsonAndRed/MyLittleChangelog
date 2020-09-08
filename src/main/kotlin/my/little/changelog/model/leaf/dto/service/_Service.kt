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
