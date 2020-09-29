package my.little.changelog.routing.version

import io.ktor.application.call
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route
import io.ktor.util.KtorExperimentalAPI
import io.ktor.util.getOrFail
import my.little.changelog.model.version.dto.service.toExternalDto
import my.little.changelog.service.version.VersionService

@KtorExperimentalAPI
fun Routing.versionRouting() {
    route("/version") {
        get {
            call.respond(VersionService.getVersions().map { it.toExternalDto() })
        }

        post {
            call.respond(VersionService.createVersion().toExternalDto())
        }
    }

    route("/version/{versionId}") {
        get {
            val idParam = call.parameters.getOrFail("versionId")
            call.respond(VersionService.getWholeVersion(idParam.toInt()))
        }
    }
}
