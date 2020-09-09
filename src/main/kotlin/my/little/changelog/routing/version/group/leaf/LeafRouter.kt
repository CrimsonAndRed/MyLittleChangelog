package my.little.changelog.routing.version.group.leaf

import io.ktor.application.call
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.post
import io.ktor.routing.put
import io.ktor.routing.route
import io.ktor.util.KtorExperimentalAPI
import io.ktor.util.getOrFail
import my.little.changelog.model.leaf.dto.external.LeafCreationDto
import my.little.changelog.model.leaf.dto.external.LeafUpdateDto
import my.little.changelog.model.leaf.dto.external.toServiceDto
import my.little.changelog.model.leaf.dto.service.toExternalDto
import my.little.changelog.service.LeafService

@KtorExperimentalAPI
fun Routing.leafRouting() {
    route("version/{versionId}/group/{groupId}/leaf") {
        post {
            val groupId = call.parameters.getOrFail("groupId").toInt()
            val versionId = call.parameters.getOrFail("versionId").toInt()
            val dto = call.receive<LeafCreationDto>()

            val leaf = LeafService.createLeaf(dto.toServiceDto(groupId, versionId))
            call.respond(leaf.toExternalDto())
        }
    }

    route("version/{versionId}/group/{groupId}/leaf/{leafId}") {
        put {
            val leafId = call.parameters.getOrFail("leafId").toInt()
            val dto = call.receive<LeafUpdateDto>()

            val leaf = LeafService.updateLeaf(dto.toServiceDto(leafId))
            call.respond(leaf.toExternalDto())
        }
    }
}
