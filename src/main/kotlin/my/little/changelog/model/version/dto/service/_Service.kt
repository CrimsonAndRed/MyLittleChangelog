package my.little.changelog.model.version.dto.service

fun ReturnedVersionDto.toExternalDto() = my.little.changelog.model.version.dto.external.ReturnedVersionDto(
    id = id,
    name = name,
    order = order
)

fun VersionCreationDto.toRepoDto() = my.little.changelog.model.version.dto.repo.VersionCreationDto(
    name = name
)
