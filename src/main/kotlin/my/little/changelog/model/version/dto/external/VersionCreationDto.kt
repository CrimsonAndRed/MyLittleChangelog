package my.little.changelog.model.version.dto.external

import kotlinx.serialization.Serializable
import my.little.changelog.model.RepoCreationDto
import my.little.changelog.model.version.Version

@Serializable
data class VersionCreationDto(
    val name: String
) : RepoCreationDto<Version> {
    override fun convertToEntity(): Version.() -> Unit = {
        name = this@VersionCreationDto.name
    }
}
