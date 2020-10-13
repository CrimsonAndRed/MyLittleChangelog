package my.little.changelog.model.group.dto.external

import kotlinx.serialization.Serializable
import my.little.changelog.model.leaf.dto.external.WholeLeafDto

@Serializable
data class WholeGroupDto(
    val id: Int,
    val vid: Int,
    val name: String,
    val realNode: Boolean,
    val groupContent: List<WholeGroupDto>,
    val leafContent: List<WholeLeafDto>,
)
