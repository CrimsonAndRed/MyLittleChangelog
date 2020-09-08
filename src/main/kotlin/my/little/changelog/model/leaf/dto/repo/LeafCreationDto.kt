package my.little.changelog.model.leaf.dto.repo

import my.little.changelog.model.group.Group
import my.little.changelog.model.version.Version

data class LeafCreationDto(
    val name: String,
    val valueType: Int,
    val value: String,
    val group: Group,
    val version: Version,
)
