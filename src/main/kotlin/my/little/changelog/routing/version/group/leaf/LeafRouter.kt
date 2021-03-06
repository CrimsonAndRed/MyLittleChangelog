package my.little.changelog.routing.version.group.leaf

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
import my.little.changelog.model.leaf.dto.external.ChangeLeafPositionDto
import my.little.changelog.model.leaf.dto.external.LeafCreationDto
import my.little.changelog.model.leaf.dto.external.LeafDeletionDto
import my.little.changelog.model.leaf.dto.external.LeafUpdateDto
import my.little.changelog.model.leaf.dto.external.toServiceDto
import my.little.changelog.model.leaf.dto.service.toExternalDto
import my.little.changelog.routing.ofEmptyResponse
import my.little.changelog.routing.ofResponse
import my.little.changelog.service.leaf.LeafService
import my.little.changelog.validator.Response
import my.little.changelog.validator.dto.LeafDtoValidator

@KtorExperimentalAPI
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
                        LeafService.createLeaf(dto.toServiceDto(groupId, versionId), principal).map { it.toExternalDto() }
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
                        LeafService.updateLeaf(dto.toServiceDto(leafId), principal)
                    }.let {
                        call.ofEmptyResponse(it)
                    }
            }
            delete {
                val principal = call.principal<CustomPrincipal>()!!
                val leafDeletionDto = LeafDeletionDto(call.parameters.getOrFail("leafId").toInt())
                val resp: Response<Unit> = LeafService.deleteLeaf(leafDeletionDto.toServiceDto(), principal)
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
