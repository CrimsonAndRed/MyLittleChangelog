package my.little.changelog.model.auth.dto.external

import kotlinx.serialization.Serializable

@Serializable
data class UserCreateDto(
    val login: String,
    val password: String
)
