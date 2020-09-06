package my.little.changelog.routing

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route
import io.ktor.routing.routing
import io.ktor.util.KtorExperimentalAPI
import io.ktor.util.getOrFail
import my.little.changelog.service.createVersion
import my.little.changelog.service.getWholeVersion

@KtorExperimentalAPI
fun Application.module() {
    routing {
        route("/") {
            get { throw RuntimeException() }
        }

        route("/version") {
            post {
                call.respond(createVersion())
            }

            get {
                val idParam = call.parameters.getOrFail("id")
                call.respond(getWholeVersion(idParam.toInt()))
            }
        }
    }
}
