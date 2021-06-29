package my.little.changelog.routing.project

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.util.*
import my.little.changelog.configuration.auth.CustomPrincipal
import my.little.changelog.model.project.dto.external.ProjectCreationDto
import my.little.changelog.model.project.dto.external.toServiceDto
import my.little.changelog.model.project.dto.service.ProjectDeletionDto
import my.little.changelog.model.project.dto.service.toExternalDto
import my.little.changelog.routing.ofEmptyResponse
import my.little.changelog.routing.ofResponse
import my.little.changelog.service.project.ProjectService
import my.little.changelog.validator.dto.ProjectDtoValidator
import my.little.changelog.validator.Response

fun Routing.projectRouting() {
    authenticate {
        route("/project") {
            get {
                val principal = call.principal<CustomPrincipal>()!!
                call.respond(ProjectService.getProjects(principal).map { it.toExternalDto() })
            }

            post {
                val principal = call.principal<CustomPrincipal>()!!
                val dto = call.receive<ProjectCreationDto>()
                call.ofResponse(
                    ProjectDtoValidator.validateNew(dto)
                        .ifValid {
                            ProjectService.createProject(dto.toServiceDto(principal)).toExternalDto()
                        }
                )
            }
        }

        route("/project/{projectId}") {
            delete {
                val principal = call.principal<CustomPrincipal>()!!
                val idParam = call.parameters.getOrFail("projectId")
                val resp: Response<Unit> = ProjectService.deleteProject(ProjectDeletionDto(idParam.toInt(), principal))
                call.ofEmptyResponse(resp)
            }
        }
    }
}
