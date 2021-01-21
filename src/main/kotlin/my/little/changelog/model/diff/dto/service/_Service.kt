package my.little.changelog.model.diff.dto.service

import my.little.changelog.model.group.dto.service.toExternalDto
import my.little.changelog.model.leaf.dto.service.toExternalDto

fun ReturnedDifferenceDto.toExternalDto() = my.little.changelog.model.diff.dto.external.ReturnedDifferenceDto(
    from = this.fromVersion,
    to = this.toVersion,
    groupContent = this.groupContent.map { it.toExternalDto() },
    leafContent = this.leafContent.map { it.toExternalDto() }
)
