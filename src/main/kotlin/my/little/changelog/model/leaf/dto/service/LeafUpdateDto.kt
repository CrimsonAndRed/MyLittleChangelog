package my.little.changelog.model.leaf.dto.service

data class LeafUpdateDto(
    val id: Int,
    val name: String,
    val valueType: Int,
    val value: String,
    val parentVid: Int
)
