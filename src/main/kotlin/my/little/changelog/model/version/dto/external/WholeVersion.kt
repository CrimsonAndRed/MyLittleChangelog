package my.little.changelog.model.version.dto.external

import kotlinx.serialization.Serializable
import my.little.changelog.model.group.dto.external.WholeGroupDto
import my.little.changelog.model.leaf.dto.external.WholeLeafDto

@Serializable
data class WholeVersion(
    val id: Int,
    val groupContent: List<WholeGroupDto>,
    val leafContent: List<WholeLeafDto>,
)
