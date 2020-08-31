package my.little.changelog.routing

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.route
import io.ktor.routing.routing

fun Application.module() {
    routing {
        route("/") {
            get {
                call.respond {
                    throw RuntimeException()
                }
//                call.respondText("HELLO WORLD!", contentType = ContentType.Text.Plain)
            }
        }
    }
}
