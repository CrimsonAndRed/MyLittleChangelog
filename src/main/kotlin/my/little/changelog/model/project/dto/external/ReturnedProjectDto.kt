package my.little.changelog.model.project.dto.external

import kotlinx.serialization.Serializable

@Serializable
data class ReturnedProjectDto(
    val id: Int,
    val name: String,
    val description: String,
    val order: Int
)
