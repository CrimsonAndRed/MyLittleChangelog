package my.little.changelog.model.version.dto.service

fun ReturnedVersionDto.toExternalDto() = my.little.changelog.model.version.dto.external.ReturnedVersionDto(
    id = id
)
