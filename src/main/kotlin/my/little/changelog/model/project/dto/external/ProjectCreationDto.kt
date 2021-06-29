package my.little.changelog.model.project.dto.external

import kotlinx.serialization.Serializable

@Serializable
data class ProjectCreationDto(
    val name: String?,
    val description: String?
)
