package my.little.changelog.model.leaf.dto.repo

import my.little.changelog.model.RepoCreationDto
import my.little.changelog.model.group.Group
import my.little.changelog.model.leaf.Leaf
import my.little.changelog.model.version.Version

data class LeafCreationDto(
    val name: String,
    val valueType: Int,
    val value: String,
    val group: Group,
    val version: Version,
) : RepoCreationDto<Leaf> {
    override fun convertToEntity(): Leaf.() -> Unit = {
        name = this@LeafCreationDto.name
        valueType = this@LeafCreationDto.valueType
        value = this@LeafCreationDto.value
        version = this@LeafCreationDto.version
        groupVid = this@LeafCreationDto.group.vid
    }
}
