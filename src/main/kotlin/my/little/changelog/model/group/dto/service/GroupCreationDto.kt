package my.little.changelog.model.group.dto.service

data class GroupCreationDto(
    val name: String,
    val vid: Int? = null,
    val parentVid: Int? = null,
    val versionId: Int
)
