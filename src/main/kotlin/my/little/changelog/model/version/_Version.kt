package my.little.changelog.model.version

import my.little.changelog.model.version.dto.VersionDto

fun Version.toDto() = VersionDto(
    id = id.value
)
