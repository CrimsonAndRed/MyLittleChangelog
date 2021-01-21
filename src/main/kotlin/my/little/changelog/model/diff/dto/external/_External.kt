package my.little.changelog.model.diff.dto.external

fun DifferenceDto.toServiceDto() = my.little.changelog.model.diff.dto.service.DifferenceDto(
    fromVersion = this.fromVersion,
    toVersion = this.toVersion,
)
