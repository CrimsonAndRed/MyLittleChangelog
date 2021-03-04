package my.little.changelog.model.auth.dto

import my.little.changelog.model.auth.dto.external.AuthDto
import my.little.changelog.model.auth.dto.external.UserCreateDto
import org.jetbrains.exposed.sql.statements.api.ExposedBlob

fun AuthDto.toServiceDto() = my.little.changelog.model.auth.dto.service.AuthDto(
    login = login,
    password = password
)

fun UserCreateDto.toServiceDto() = my.little.changelog.model.auth.dto.service.UserCreateDto(
    login = login,
    password = password
)

fun my.little.changelog.model.auth.dto.service.UserCreateDto.toRepoDto(f: (String) -> ExposedBlob) = my.little.changelog.model.auth.dto.repo.UserCreationDto(
    login = login,
    password = f(password)
)
