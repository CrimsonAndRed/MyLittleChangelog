package my.little.changelog

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.config.*
import io.ktor.features.*
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.request.path
import io.ktor.response.respond
import io.ktor.serialization.json
import io.ktor.util.*
import my.little.changelog.configuration.Json
import my.little.changelog.configuration.auth.CustomPrincipal
import my.little.changelog.configuration.auth.JwtConfig
import my.little.changelog.exception.ForbiddenException
import my.little.changelog.exception.UnauthException
import org.slf4j.event.Level
import kotlin.collections.set

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@KtorExperimentalAPI
fun Application.module(testing: Boolean = false) {

    JwtConfig.audience = environment.config.property("jwt.audience").getString()
    JwtConfig.issuer = environment.config.property("jwt.issuer").getString()
    JwtConfig.subject = environment.config.property("jwt.subject").getString()
    JwtConfig.secret = environment.config.property("jwt.secret").getString()

    install(ContentNegotiation) {
        json(
            Json,
            ContentType.Application.Json
        )
    }

    install(CallLogging) {
        level = Level.INFO
        filter { call -> call.request.path().startsWith("/") }
    }

    install(CORS) {
        method(HttpMethod.Options)
        method(HttpMethod.Put)
        method(HttpMethod.Delete)
        method(HttpMethod.Patch)
        header("Authorization")
        allowNonSimpleContentTypes = true
        anyHost()
        allowCredentials = true
        allowNonSimpleContentTypes = true
    }

    install(StatusPages) {
        exception<UnauthException> {
            environment.log.error("unauthorized")
            call.respond(HttpStatusCode.Unauthorized)
        }
        exception<ForbiddenException> {
            environment.log.error("forbidden")
            call.respond(HttpStatusCode.Forbidden)
        }
        exception<Throwable> {
            environment.log.error(it)
            call.respond(HttpStatusCode.InternalServerError)
        }
    }

    install(Authentication) {
        jwt {
            realm = environment.config.property("jwt.realm").getString()
            verifier(JwtConfig.generateVerifier())
            validate { credential ->
                val userId = credential.payload.getClaim("id").asInt()
                when {
                    userId > 0 -> CustomPrincipal(credential.payload, userId)
                    else -> null
                }
            }
        }
    }
}
