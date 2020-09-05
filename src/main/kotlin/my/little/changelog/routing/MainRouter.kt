package my.little.changelog.routing

import io.ktor.application.Application
import io.ktor.routing.get
import io.ktor.routing.route
import io.ktor.routing.routing

fun Application.module() {
    routing {
        route("/") {
            get { throw RuntimeException() }
//                call.respondText("HELLO WORLD!", contentType = ContentType.Text.Plain)
        }
    }
}
