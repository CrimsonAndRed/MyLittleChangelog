package my.little.changelog.routing.auth

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.util.*
import my.little.changelog.model.auth.dto.external.AuthDto
import my.little.changelog.model.auth.dto.external.Token
import my.little.changelog.model.auth.dto.external.UserCreateDto
import my.little.changelog.model.auth.dto.toServiceDto
import my.little.changelog.service.auth.AuthService

@KtorExperimentalAPI
fun Routing.authRouting() {
    route("/auth") {
        post {
            val dto = call.receive<AuthDto>()
            val token = AuthService.auth(dto.toServiceDto())
            call.respond(Token(token))
        }
    }

    route("/user") {
        post {
            val dto = call.receive<UserCreateDto>()
            AuthService.newUser(dto.toServiceDto())
            call.response.status(HttpStatusCode.NoContent)
        }
    }
}
