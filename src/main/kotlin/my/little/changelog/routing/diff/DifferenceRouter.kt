package my.little.changelog.routing.diff

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.util.*
import my.little.changelog.configuration.auth.CustomPrincipal
import my.little.changelog.model.diff.dto.external.DifferenceDto
import my.little.changelog.model.diff.dto.external.toServiceDto
import my.little.changelog.model.diff.dto.service.toExternalDto
import my.little.changelog.service.diff.DifferenceService

fun Routing.differenceRouting() {
    authenticate {
        route("/difference") {
            get {
                val principal = call.principal<CustomPrincipal>()!!
                val from = call.request.queryParameters.getOrFail("from").toInt()
                val to = call.request.queryParameters.getOrFail("to").toInt()

                val dto = DifferenceDto(from, to)
                call.respond(DifferenceService.findDifference(dto.toServiceDto(principal)).toExternalDto())
            }
        }
    }
}
