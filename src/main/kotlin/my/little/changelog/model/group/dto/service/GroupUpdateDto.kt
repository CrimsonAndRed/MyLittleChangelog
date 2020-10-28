package my.little.changelog.model.group.dto.service

data class GroupUpdateDto(
    val id: Int,
    val name: String,
    val parentVid: Int? = null
)
