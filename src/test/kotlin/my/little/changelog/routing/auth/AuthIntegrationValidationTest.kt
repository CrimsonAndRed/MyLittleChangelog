package my.little.changelog.routing.auth

import io.ktor.http.*
import io.ktor.server.testing.*
import io.ktor.util.*
import kotlinx.serialization.encodeToString
import my.little.changelog.configuration.Json
import my.little.changelog.model.auth.dto.external.AuthDto
import my.little.changelog.model.auth.dto.external.UserCreationDto
import my.little.changelog.routing.AbstractIntegrationTest
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

@KtorExperimentalAPI
internal class AuthIntegrationValidationTest : AbstractIntegrationTest() {
    @Test
    fun `Test Auth With Blank Name Failure`() {
        testApplication {
            transaction {
                val dto: AuthDto = AuthDto(
                    login = "",
                    password = "Password"
                )

                with(
                    handleRequest(HttpMethod.Post, "/auth") {
                        addHeader("Content-Type", "application/json")
                        setBody(Json.encodeToString(dto))
                    }
                ) {
                    Assertions.assertEquals(HttpStatusCode.BadRequest, response.status())
                }
            }
        }
    }

    @Test
    fun `Test Auth With Blank Password Failure`() {
        testApplication {
            transaction {
                val dto: AuthDto = AuthDto(
                    login = "Login",
                    password = ""
                )

                with(
                    handleRequest(HttpMethod.Post, "/auth") {
                        addHeader("Content-Type", "application/json")
                        setBody(Json.encodeToString(dto))
                    }
                ) {
                    Assertions.assertEquals(HttpStatusCode.BadRequest, response.status())
                }
            }
        }
    }

    @Test
    fun `Test New User With Blank Login Failure`() {
        testApplication {
            transaction {
                val dto: UserCreationDto = UserCreationDto(
                    login = "",
                    password = "Password"
                )

                with(
                    handleRequest(HttpMethod.Post, "/user") {
                        addHeader("Content-Type", "application/json")
                        setBody(Json.encodeToString(dto))
                    }
                ) {
                    Assertions.assertEquals(HttpStatusCode.BadRequest, response.status())
                }
            }
        }
    }

    @Test
    fun `Test New User With Blank Password Failure`() {
        testApplication {
            transaction {
                val dto: UserCreationDto = UserCreationDto(
                    login = "Login",
                    password = ""
                )

                with(
                    handleRequest(HttpMethod.Post, "/user") {
                        addHeader("Content-Type", "application/json")
                        setBody(Json.encodeToString(dto))
                    }
                ) {
                    Assertions.assertEquals(HttpStatusCode.BadRequest, response.status())
                }
            }
        }
    }
}
