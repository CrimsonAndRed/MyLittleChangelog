package my.little.changelog.model.group.dto.service

data class GroupUpdateDto(
    val id: Int,
    val name: String,
    val parentId: Int? = null
)