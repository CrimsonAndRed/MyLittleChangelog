package my.little.changelog.model.version.dto.external

import kotlinx.serialization.Serializable
import my.little.changelog.model.group.dto.external.WholeGroupDto

@Serializable
data class PreviousVersionsDTO(
    val groupContent: List<WholeGroupDto>
)
