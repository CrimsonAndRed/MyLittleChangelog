package my.little.changelog.model.auth.dto.external

import kotlinx.serialization.Serializable

@Serializable
data class AuthDto(
    val login: String,
    val password: String
)
