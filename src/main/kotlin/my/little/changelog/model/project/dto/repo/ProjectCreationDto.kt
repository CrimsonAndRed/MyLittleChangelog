package my.little.changelog.model.project.dto.repo

import my.little.changelog.model.RepoCreationDto
import my.little.changelog.model.auth.User
import my.little.changelog.model.project.Project

data class ProjectCreationDto(
    val name: String,
    val description: String,
    val user: User
) : RepoCreationDto<Project> {
    override fun convertToEntity(): Project.() -> Unit = {
        name = this@ProjectCreationDto.name
        description = this@ProjectCreationDto.description
        user = this@ProjectCreationDto.user
    }
}
