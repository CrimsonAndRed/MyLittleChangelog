package my.little.changelog.routing.auth

import io.ktor.application.*
import io.ktor.request.*
import io.ktor.routing.*
import io.ktor.util.*
import my.little.changelog.model.auth.dto.external.AuthDto
import my.little.changelog.model.auth.dto.external.Token
import my.little.changelog.model.auth.dto.external.UserCreationDto
import my.little.changelog.model.auth.dto.toServiceDto
import my.little.changelog.routing.ofEmptyResponse
import my.little.changelog.routing.ofResponse
import my.little.changelog.service.auth.AuthService
import my.little.changelog.validator.dto.AuthDtoValidator

@KtorExperimentalAPI
fun Routing.authRouting() {
    route("/auth") {
        post {
            val dto = call.receive<AuthDto>()
            AuthDtoValidator.validateAuth(dto)
                .ifValid {
                    val token = AuthService.auth(dto.toServiceDto())
                    Token(token)
                }.let {
                    call.ofResponse(it)
                }
        }
    }

    route("/user") {
        post {
            val dto = call.receive<UserCreationDto>()
            AuthDtoValidator.validateNewUser(dto)
                .ifValid {
                    AuthService.newUser(dto.toServiceDto())
                }.let {
                    call.ofEmptyResponse(it)
                }
        }
    }
}
