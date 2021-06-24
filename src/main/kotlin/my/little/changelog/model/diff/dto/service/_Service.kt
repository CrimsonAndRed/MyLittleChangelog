package my.little.changelog.model.diff.dto.service

import my.little.changelog.model.group.dto.service.toExternalDto
import my.little.changelog.model.leaf.dto.service.toExternalDto
import my.little.changelog.model.version.dto.service.toExternalDto

fun ReturnedDifferenceDto.toExternalDto() = my.little.changelog.model.diff.dto.external.ReturnedDifferenceDto(
    from = this.fromVersion.toExternalDto(),
    to = this.toVersion.toExternalDto(),
    groupContent = this.groupContent.map { it.toExternalDto() },
    leafContent = this.leafContent.map { it.toExternalDto() }
)
