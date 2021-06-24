package my.little.changelog.model.auth.dto

import my.little.changelog.model.auth.dto.external.AuthDto
import my.little.changelog.model.auth.dto.external.UserCreationDto
import org.jetbrains.exposed.sql.statements.api.ExposedBlob

fun AuthDto.toServiceDto() = my.little.changelog.model.auth.dto.service.AuthDto(
    login = login,
    password = password
)

fun UserCreationDto.toServiceDto() = my.little.changelog.model.auth.dto.service.UserCreationDto(
    login = login,
    password = password
)

fun my.little.changelog.model.auth.dto.service.UserCreationDto.toRepoDto(f: (String) -> ExposedBlob) = my.little.changelog.model.auth.dto.repo.UserCreationDto(
    login = login,
    password = f(password)
)
