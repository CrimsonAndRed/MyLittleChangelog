package my.little.changelog.model.leaf.dto.service

data class LeafDifferenceDto(
    val id: Int,
    val vid: Int,
    val name: String,
    val valueType: Int,
    val value: String,
)
