package my.little.changelog.routing

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.request.receive
import io.ktor.request.receiveText
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route
import io.ktor.routing.routing
import io.ktor.util.Identity.decode
import io.ktor.util.KtorExperimentalAPI
import io.ktor.util.getOrFail
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import my.little.changelog.model.leaf.dto.external.LeafCreationDto
import my.little.changelog.model.leaf.dto.external.toInternal
import my.little.changelog.model.leaf.toExternal
import my.little.changelog.model.version.toDto
import my.little.changelog.service.LeafService
import my.little.changelog.service.VersionService

@KtorExperimentalAPI
fun Application.module() {
    routing {
        route("/") {
            get { throw RuntimeException() }
        }

        route("version/{versionId}/group/{groupId}/leaf") {
            post {
                val groupId = call.parameters.getOrFail("groupId").toInt()
                val versionId = call.parameters.getOrFail("versionId").toInt()

                val dto = call.receive<LeafCreationDto>()
                val leaf = LeafService.createLeaf(dto.toInternal(groupId, versionId))
                call.respond(leaf.toExternal())
            }
        }

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
}
