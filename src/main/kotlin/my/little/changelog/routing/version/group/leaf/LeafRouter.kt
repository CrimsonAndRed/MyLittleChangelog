package my.little.changelog.routing.version.group.leaf

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.request.*
import io.ktor.routing.*
import io.ktor.util.*
import my.little.changelog.configuration.auth.CustomPrincipal
import my.little.changelog.model.leaf.dto.external.*
import my.little.changelog.model.leaf.dto.service.toExternalDto
import my.little.changelog.routing.ofEmptyResponse
import my.little.changelog.routing.ofResponse
import my.little.changelog.service.leaf.LeafService
import my.little.changelog.validator.Response
import my.little.changelog.validator.dto.LeafDtoValidator

fun Routing.leafRouting() {
    authenticate {
        route("version/{versionId}/group/{groupId}/leaf") {
            post {
                val principal = call.principal<CustomPrincipal>()!!
                val groupId = call.parameters.getOrFail("groupId").toInt()
                val versionId = call.parameters.getOrFail("versionId").toInt()
                val dto = call.receive<LeafCreationDto>()
                LeafDtoValidator.validateDtoNew(dto)
                    .ifValidResponse {
                        LeafService.createLeaf(dto.toServiceDto(groupId, versionId, principal)).map { it.toExternalDto() }
                    }.let {
                        call.ofResponse(it)
                    }
            }
        }

        route("version/{versionId}/group/{groupId}/leaf/{leafId}") {
            put {
                val principal = call.principal<CustomPrincipal>()!!
                val leafId = call.parameters.getOrFail("leafId").toInt()
                val dto = call.receive<LeafUpdateDto>()
                LeafDtoValidator.validateDtoUpdate(dto)
                    .ifValidResponse {
                        LeafService.updateLeaf(dto.toServiceDto(leafId, principal))
                    }.let {
                        call.ofEmptyResponse(it)
                    }
            }
            delete {
                val principal = call.principal<CustomPrincipal>()!!
                val leafDeletionDto = LeafDeletionDto(call.parameters.getOrFail("leafId").toInt())
                val dropCompletely = call.request.queryParameters["completely"]?.toBoolean() ?: false
                val resp: Response<Unit> = LeafService.deleteLeaf(leafDeletionDto.toServiceDto(principal, dropCompletely))
                call.ofEmptyResponse(resp)
            }
        }

        route("version/{versionId}/group/{groupId}/leaf/{leafId}/position") {
            patch {
                val principal = call.principal<CustomPrincipal>()!!
                val leafId = call.parameters.getOrFail("leafId").toInt()
                val changePositionDto = call.receive<ChangeLeafPositionDto>()

                val resp = LeafService.changePosition(leafId, changePositionDto.changeAgainstId, principal)
                call.ofEmptyResponse(resp)
            }
        }
    }
}
