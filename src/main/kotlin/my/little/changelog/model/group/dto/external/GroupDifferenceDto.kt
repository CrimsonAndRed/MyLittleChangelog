package my.little.changelog.model.group.dto.external
import kotlinx.serialization.Serializable
import my.little.changelog.model.leaf.dto.external.LeafDifferenceDto

@Serializable
data class GroupDifferenceDto(
    val id: Int,
    val vid: Int,
    val name: String,
    val groupContent: List<GroupDifferenceDto>,
    val leafContent: List<LeafDifferenceDto>,
)