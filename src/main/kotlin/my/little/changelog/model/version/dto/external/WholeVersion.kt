package my.little.changelog.model.version.dto.external

import kotlinx.serialization.Serializable
import my.little.changelog.model.group.dto.external.WholeGroupDto

@Serializable
data class WholeVersion(
    val id: Int,
    val canChange: Boolean,
    val groupContent: List<WholeGroupDto>,
    val name: String,
    val projectId: Int
)
