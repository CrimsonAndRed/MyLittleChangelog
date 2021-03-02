package my.little.changelog.model.version.dto.external

import kotlinx.serialization.Serializable

@Serializable
data class ReturnedVersionDto(
    val id: Int,
    val name: String,
    val order: Int
)
