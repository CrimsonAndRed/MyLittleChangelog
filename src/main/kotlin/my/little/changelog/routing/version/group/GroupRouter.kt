package my.little.changelog.routing.version.group

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.request.*
import io.ktor.routing.*
import io.ktor.util.*
import my.little.changelog.configuration.auth.CustomPrincipal
import my.little.changelog.model.group.dto.external.*
import my.little.changelog.model.group.dto.service.toExternalDto
import my.little.changelog.routing.ofEmptyResponse
import my.little.changelog.routing.ofResponse
import my.little.changelog.service.group.GroupService
import my.little.changelog.validator.dto.GroupDtoValidator

fun Routing.groupRouting() {
    authenticate {
        route("/version/{versionId}/group") {
            post {
                val principal = call.principal<CustomPrincipal>()!!
                val versionId = call.parameters.getOrFail("versionId").toInt()
                val dto = call.receive<GroupCreationDto>()
                GroupDtoValidator.validateDtoNew(dto)
                    .ifValidResponse {
                        GroupService.createGroup(dto.toServiceDto(versionId, principal)).map { it.toExternalDto() }
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
                        GroupService.updateGroup(dto.toServiceDto(groupId, principal))
                    }.let {
                        call.ofEmptyResponse(it)
                    }
            }

            delete {
                val principal = call.principal<CustomPrincipal>()!!
                val groupId = call.parameters.getOrFail("groupId").toInt()
                val dropHierarchy = call.request.queryParameters["hierarchy"]?.toBoolean() ?: true
                val dropCompletely = call.request.queryParameters["completely"]?.toBoolean() ?: false
                val resp = GroupService.deleteGroup(GroupDeletionDto(groupId, principal), dropHierarchy, dropCompletely)
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
