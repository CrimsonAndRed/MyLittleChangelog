package my.little.changelog.model.version.dto.repo

import my.little.changelog.model.RepoCreationDto
import my.little.changelog.model.version.Version

data class VersionCreationDto(
    val name: String
) : RepoCreationDto<Version> {
    override fun convertToEntity(): Version.() -> Unit = {
        name = this@VersionCreationDto.name
    }
}
