package my.little.changelog.routing.diff

import io.ktor.application.call
import io.ktor.auth.*
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.route
import io.ktor.util.KtorExperimentalAPI
import io.ktor.util.getOrFail
import my.little.changelog.model.diff.dto.external.DifferenceDto
import my.little.changelog.model.diff.dto.external.toServiceDto
import my.little.changelog.model.diff.dto.service.toExternalDto
import my.little.changelog.service.diff.DifferenceService

@KtorExperimentalAPI
fun Routing.differenceRouting() {
    authenticate {
        route("/difference") {
            get {
                val from = call.request.queryParameters.getOrFail("from").toInt()
                val to = call.request.queryParameters.getOrFail("to").toInt()

                val dto = DifferenceDto(from, to)
                call.respond(DifferenceService.findDifference(dto.toServiceDto()).toExternalDto())
            }
        }
    }
}
