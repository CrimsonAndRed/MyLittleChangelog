package my.little.changelog.model.group.dto.service

data class GroupCreationDto(
    val name: String,
    val vid: Int? = null,
    val parentId: Int? = null,
    val versionId: Int
)
