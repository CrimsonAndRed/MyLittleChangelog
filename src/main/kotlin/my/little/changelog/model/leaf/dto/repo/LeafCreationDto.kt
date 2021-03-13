package my.little.changelog.model.leaf.dto.repo

import my.little.changelog.model.RepoCreationDto
import my.little.changelog.model.group.Group
import my.little.changelog.model.leaf.Leaf
import my.little.changelog.model.version.Version

data class LeafCreationDto(
    val vid: Int?,
    val name: String,
    val valueType: Int,
    val value: String,
    val group: Group,
    val version: Version,
    val isDeleted: Boolean = false
) : RepoCreationDto<Leaf> {
    override fun convertToEntity(): Leaf.() -> Unit = {
        this@LeafCreationDto.vid?.let { vid = it }
        name = this@LeafCreationDto.name
        valueType = this@LeafCreationDto.valueType
        value = this@LeafCreationDto.value
        version = this@LeafCreationDto.version
        groupVid = this@LeafCreationDto.group.vid
        isDeleted = this@LeafCreationDto.isDeleted
    }
}
