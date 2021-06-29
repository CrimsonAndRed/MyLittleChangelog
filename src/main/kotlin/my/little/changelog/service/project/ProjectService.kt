package my.little.changelog.service.project

import my.little.changelog.configuration.auth.CustomPrincipal
import my.little.changelog.model.project.dto.service.ProjectCreationDto
import my.little.changelog.model.project.dto.service.ProjectDeletionDto
import my.little.changelog.model.project.dto.service.ReturnedProjectDto
import my.little.changelog.model.project.dto.service.toRepoDto
import my.little.changelog.model.project.toReturnedDto
import my.little.changelog.model.version.dto.service.VersionDeletionDto
import my.little.changelog.persistence.repo.ProjectRepo
import my.little.changelog.persistence.repo.VersionRepo
import my.little.changelog.service.version.VersionService
import my.little.changelog.validator.AuthValidator
import my.little.changelog.validator.Response
import org.jetbrains.exposed.sql.transactions.transaction

object ProjectService {

    fun getProjects(cp: CustomPrincipal): List<ReturnedProjectDto> = transaction {
        ProjectRepo.findByUser(cp.user).sortedBy {
            it.order
        }.map {
            it.toReturnedDto()
        }
    }

    fun createProject(projectCreationDto: ProjectCreationDto): ReturnedProjectDto = transaction {
        ProjectRepo.create(projectCreationDto.toRepoDto(projectCreationDto.principal.user)).toReturnedDto()
    }

    fun deleteProject(deletionDto: ProjectDeletionDto): Response<Unit> = transaction {
        val project = ProjectRepo.findById(deletionDto.id)
        val versions = VersionRepo.findByProject(project)

        AuthValidator.validateAuthority(project.user, deletionDto.principal.user)
            .ifValid {
                versions.sortedByDescending {
                    it.order
                }.forEach {
                    VersionService.deleteVersion(VersionDeletionDto(it.id.value, deletionDto.principal))
                }

                ProjectRepo.delete(project)
            }
    }
}
