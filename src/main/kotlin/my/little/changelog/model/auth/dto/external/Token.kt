package my.little.changelog.model.auth.dto.external

import kotlinx.serialization.Serializable

@Serializable
data class Token(
    val token: String
)
