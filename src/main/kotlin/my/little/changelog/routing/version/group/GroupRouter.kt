package my.little.changelog.routing.version.group

import io.ktor.application.call
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.post
import io.ktor.routing.put
import io.ktor.routing.route
import io.ktor.util.KtorExperimentalAPI
import io.ktor.util.getOrFail
import my.little.changelog.model.group.dto.external.GroupCreationDto
import my.little.changelog.model.group.dto.external.GroupUpdateDto
import my.little.changelog.model.group.dto.external.toServiceDto
import my.little.changelog.service.GroupService

@KtorExperimentalAPI
fun Routing.groupRouting() {
    route("/version/{versionId}/group") {
        post {
            val versionId = call.parameters.getOrFail("versionId").toInt()

            val dto = call.receive<GroupCreationDto>()
            val group = GroupService.createGroup(dto.toServiceDto(versionId))
            call.respond(group)
        }
    }

    route("/version/{versionId}/group/{groupId}") {
        put {
//            val versionId = call.parameters.getOrFail("versionId").toInt()
            val groupId = call.parameters.getOrFail("groupId").toInt()

            val dto = call.receive<GroupUpdateDto>()
            val group = GroupService.updateGroup(dto.toServiceDto(groupId))
            call.respond(group)
        }
    }
}
