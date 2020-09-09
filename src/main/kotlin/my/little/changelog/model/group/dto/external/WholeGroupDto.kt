package my.little.changelog.model.group.dto.external

import kotlinx.serialization.Serializable
import my.little.changelog.model.leaf.dto.external.WholeLeafDto

@Serializable
data class WholeGroupDto(
    val id: Int,
    val vid: Int,
    val version: Int,
    val name: String,
    val groupContent: List<WholeGroupDto>,
    val leafContent: List<WholeLeafDto>,
)
