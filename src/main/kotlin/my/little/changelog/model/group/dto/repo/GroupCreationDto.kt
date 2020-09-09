package my.little.changelog.model.group.dto.repo

import my.little.changelog.model.version.Version

data class GroupCreationDto(
    val name: String,
    val vid: Int? = null,
    val parentVid: Int? = null,
    val version: Version
)
