package my.little.changelog.model.group.dto.repo

import my.little.changelog.model.RepoCreationDto
import my.little.changelog.model.group.Group
import my.little.changelog.model.version.Version

data class GroupCreationDto(
    val name: String,
    val vid: Int? = null,
    val parentVid: Int? = null,
    val version: Version,
    val order: Int?,
    val isDeleted: Boolean = false
) : RepoCreationDto<Group> {
    override fun convertToEntity(): Group.() -> Unit = {
        name = this@GroupCreationDto.name
        this@GroupCreationDto.vid?.let { vid = it }
        parentVid = this@GroupCreationDto.parentVid
        version = this@GroupCreationDto.version
        this@GroupCreationDto.order?.let { order = it }
        isDeleted = this@GroupCreationDto.isDeleted
    }
}
