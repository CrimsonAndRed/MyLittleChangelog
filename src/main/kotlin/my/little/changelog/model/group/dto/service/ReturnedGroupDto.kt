package my.little.changelog.model.group.dto.service

data class ReturnedGroupDto(
    val id: Int,
    val vid: Int,
    val name: String,
    val parentVid: Int? = null
)
