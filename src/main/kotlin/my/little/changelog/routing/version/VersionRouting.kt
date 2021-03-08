package my.little.changelog.routing.version

import io.ktor.application.call
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.delete
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route
import io.ktor.util.KtorExperimentalAPI
import io.ktor.util.getOrFail
import my.little.changelog.configuration.auth.CustomPrincipal
import my.little.changelog.model.version.dto.external.VersionCreationDto
import my.little.changelog.model.version.dto.external.toServiceDto
import my.little.changelog.model.version.dto.service.VersionDeletionDto
import my.little.changelog.model.version.dto.service.toExternalDto
import my.little.changelog.routing.ofEmptyResponse
import my.little.changelog.service.version.VersionService
import my.little.changelog.validator.Response

@KtorExperimentalAPI
fun Routing.versionRouting() {
    authenticate {
        route("/version") {
            get {
                val principal = call.principal<CustomPrincipal>()!!
                call.respond(VersionService.getVersions(principal).map { it.toExternalDto() })
            }

            post {
                val principal = call.principal<CustomPrincipal>()!!
                val dto = call.receive<VersionCreationDto>()
                call.respond(VersionService.createVersion(dto.toServiceDto(principal)).toExternalDto())
            }

            route("/previous") {
                get {
                    val principal = call.principal<CustomPrincipal>()!!
                    call.respond(VersionService.getPreviousVersions(principal))
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
