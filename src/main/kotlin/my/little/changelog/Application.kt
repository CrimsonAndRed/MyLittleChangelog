package my.little.changelog

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.serialization.*
import io.ktor.util.*
import my.little.changelog.configuration.Json
import my.little.changelog.configuration.auth.CustomPrincipal
import my.little.changelog.configuration.auth.JwtConfig
import my.little.changelog.exception.ForbiddenException
import my.little.changelog.exception.UnauthException
import my.little.changelog.model.auth.User
import my.little.changelog.model.auth.Users
import my.little.changelog.persistence.repo.AuthRepo
import org.jetbrains.exposed.dao.id.EntityID
import org.slf4j.event.Level

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@KtorExperimentalAPI
fun Application.module(testing: Boolean = false, authTest: Boolean = false) {

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

    if (authTest) {
        install(Authentication) {
            jwt {
                JwtConfig.audience = environment.config.property("jwt.audience").getString()
                JwtConfig.issuer = environment.config.property("jwt.issuer").getString()
                JwtConfig.subject = environment.config.property("jwt.subject").getString()
                JwtConfig.secret = environment.config.property("jwt.secret").getString()
                realm = environment.config.property("jwt.realm").getString()
                verifier(JwtConfig.generateVerifier())
                validate { credential ->
                    CustomPrincipal(credential.payload, getTestUser())
                }
            }
        }
    } else {
        install(Authentication) {
            jwt {
                JwtConfig.audience = environment.config.property("jwt.audience").getString()
                JwtConfig.issuer = environment.config.property("jwt.issuer").getString()
                JwtConfig.subject = environment.config.property("jwt.subject").getString()
                JwtConfig.secret = environment.config.property("jwt.secret").getString()
                realm = environment.config.property("jwt.realm").getString()
                verifier(JwtConfig.generateVerifier())
                validate { credential ->
                    val userClaim = credential.payload.getClaim("id")
                    when {
                        userClaim.isNull -> null
                        else -> {
                            CustomPrincipal(credential.payload, AuthRepo.findById(userClaim.asInt()))
                        }
                    }
                }
            }
        }
    }
}

public fun getTestUser(): User {
    return User(EntityID(0, Users))
}

