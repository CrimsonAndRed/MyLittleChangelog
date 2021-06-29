package my.little.changelog.model.version.dto.repo

import my.little.changelog.model.RepoCreationDto
import my.little.changelog.model.auth.User
import my.little.changelog.model.project.Project
import my.little.changelog.model.version.Version

data class VersionCreationDto(
    val name: String,
    val user: User,
    val project: Project
) : RepoCreationDto<Version> {
    override fun convertToEntity(): Version.() -> Unit = {
        name = this@VersionCreationDto.name
        user = this@VersionCreationDto.user
        project = this@VersionCreationDto.project
    }
}
