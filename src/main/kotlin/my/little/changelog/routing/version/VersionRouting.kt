package my.little.changelog.routing.version

import io.ktor.application.call
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route
import io.ktor.util.KtorExperimentalAPI
import io.ktor.util.getOrFail
import my.little.changelog.model.version.toDto
import my.little.changelog.service.VersionService

@KtorExperimentalAPI
fun Routing.versionRouting() {
    route("/version") {
        post {
            call.respond(VersionService.createVersion().toDto())
        }

        get {
            val idParam = call.request.queryParameters.getOrFail("id")
            call.respond(VersionService.getWholeVersion(idParam.toInt()))
        }
    }
}
