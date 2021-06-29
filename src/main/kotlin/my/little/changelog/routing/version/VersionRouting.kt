package my.little.changelog.routing.version

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.util.*
import my.little.changelog.configuration.auth.CustomPrincipal
import my.little.changelog.model.version.dto.external.VersionCreationDto
import my.little.changelog.model.version.dto.external.toServiceDto
import my.little.changelog.model.version.dto.service.VersionDeletionDto
import my.little.changelog.model.version.dto.service.toExternalDto
import my.little.changelog.routing.ofEmptyResponse
import my.little.changelog.service.version.VersionService
import my.little.changelog.validator.Response

fun Routing.versionRouting() {
    authenticate {
        route("project/{projectId}/version") {
            get {
                val principal = call.principal<CustomPrincipal>()!!
                val projectId = call.parameters.getOrFail("projectId")
                call.respond(VersionService.getVersions(projectId.toInt(), principal).map { it.toExternalDto() })
            }

            post {
                val principal = call.principal<CustomPrincipal>()!!
                val dto = call.receive<VersionCreationDto>()
                val projectId = call.parameters.getOrFail("projectId")
                call.respond(VersionService.createVersion(dto.toServiceDto(projectId.toInt(), principal)).toExternalDto())
            }

            route("/previous") {
                get {
                    val principal = call.principal<CustomPrincipal>()!!
                    val projectId = call.parameters.getOrFail("projectId")
                    call.respond(VersionService.getPreviousVersions(projectId.toInt(), principal))
                }
            }
        }

        route("/version/{versionId}") {
            get {
                val principal = call.principal<CustomPrincipal>()!!
                val idParam = call.parameters.getOrFail("versionId")
                call.respond(VersionService.getWholeVersion(idParam.toInt(), principal))
            }
            delete {
                val principal = call.principal<CustomPrincipal>()!!
                val idParam = call.parameters.getOrFail("versionId")
                val resp: Response<Unit> = VersionService.deleteVersion(VersionDeletionDto(idParam.toInt(), principal))
                call.ofEmptyResponse(resp)
            }
        }
    }
}
