package my.little.changelog.routing.version.group

import io.ktor.application.call
import io.ktor.auth.*
import io.ktor.request.receive
import io.ktor.routing.Routing
import io.ktor.routing.delete
import io.ktor.routing.patch
import io.ktor.routing.post
import io.ktor.routing.put
import io.ktor.routing.route
import io.ktor.util.KtorExperimentalAPI
import io.ktor.util.getOrFail
import my.little.changelog.configuration.auth.CustomPrincipal
import my.little.changelog.model.group.dto.external.ChangeGroupPositionDto
import my.little.changelog.model.group.dto.external.GroupCreationDto
import my.little.changelog.model.group.dto.external.GroupDeletionDto
import my.little.changelog.model.group.dto.external.GroupUpdateDto
import my.little.changelog.model.group.dto.external.toServiceDto
import my.little.changelog.model.group.dto.service.toExternalDto
import my.little.changelog.routing.ofEmptyResponse
import my.little.changelog.routing.ofResponse
import my.little.changelog.service.group.GroupService
import my.little.changelog.validator.dto.GroupDtoValidator

@KtorExperimentalAPI
fun Routing.groupRouting() {
    authenticate {
        route("/version/{versionId}/group") {
            post {
                val principal = call.principal<CustomPrincipal>()!!
                val versionId = call.parameters.getOrFail("versionId").toInt()
                val dto = call.receive<GroupCreationDto>()
                GroupDtoValidator.validateDtoNew(dto)
                    .ifValidResponse {
                        GroupService.createGroup(dto.toServiceDto(versionId), principal).map { it.toExternalDto() }
                    }.let {
                        call.ofResponse(it)
                    }
            }
        }

        route("/version/{versionId}/group/{groupId}") {
            put {
                val principal = call.principal<CustomPrincipal>()!!
                val groupId = call.parameters.getOrFail("groupId").toInt()
                val dto = call.receive<GroupUpdateDto>()
                GroupDtoValidator.validateDtoUpdate(dto)
                    .ifValidResponse {
                        GroupService.updateGroup(dto.toServiceDto(groupId), principal)
                    }.let {
                        call.ofEmptyResponse(it)
                    }
            }

            delete {
                val principal = call.principal<CustomPrincipal>()!!
                val groupId = call.parameters.getOrFail("groupId").toInt()
                val dropHierarchy = call.request.queryParameters["hierarchy"]?.toBoolean() ?: true
                val resp = GroupService.deleteGroup(GroupDeletionDto(groupId), principal, dropHierarchy)
                call.ofEmptyResponse(resp)
            }
        }

        route("version/{versionId}/group/{groupId}/position") {
            patch {
                val principal = call.principal<CustomPrincipal>()!!
                val leafId = call.parameters.getOrFail("groupId").toInt()
                val changePositionDto = call.receive<ChangeGroupPositionDto>()

                val resp = GroupService.changePosition(leafId, changePositionDto.changeAgainstId, principal)
                call.ofEmptyResponse(resp)
            }
        }
    }
}
