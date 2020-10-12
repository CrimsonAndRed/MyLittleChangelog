package my.little.changelog.model.leaf.dto.service

data class LeafCreationDto(
    val vid: Int?,
    val name: String,
    val valueType: Int,
    val value: String,
    val groupId: Int,
    val versionId: Int,
)
