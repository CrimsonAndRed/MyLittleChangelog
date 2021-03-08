package my.little.changelog.routing.auth

import io.ktor.http.*
import io.ktor.server.testing.*
import io.ktor.util.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import my.little.changelog.configuration.Json
import my.little.changelog.model.auth.dto.external.AuthDto
import my.little.changelog.model.auth.dto.external.Token
import my.little.changelog.model.auth.dto.external.UserCreationDto
import my.little.changelog.routing.AbstractIntegrationTest
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

@KtorExperimentalAPI
class AuthIntegrationTest : AbstractIntegrationTest() {

    @Test
    fun `Test Auth User Success`() {
        testApplication {
            transaction {
                val pw = "Password"
                val login = "Login"
                createUser(login = login, password = pw)
                val dto: AuthDto = AuthDto(
                    login = login,
                    password = pw
                )

                with(
                    handleRequest(HttpMethod.Post, "/auth") {
                        addHeader("Content-Type", "application/json")
                        setBody(Json.encodeToString(dto))
                    }
                ) {
                    Assertions.assertEquals(HttpStatusCode.OK, response.status())
                    val token: Token = Json.decodeFromString(response.content!!)
                    Assertions.assertNotNull(token)
                }
            }
        }
    }

    @Test
    fun `Test Auth User Wrong Credentials Failure`() {
        testApplication {
            transaction {
                val pw = "Password"
                val login = "Login"
                createUser(login = login, password = pw)
                val dto: AuthDto = AuthDto(
                    login = login,
                    password = "1_$pw"
                )

                with(
                    handleRequest(HttpMethod.Post, "/auth") {
                        addHeader("Content-Type", "application/json")
                        setBody(Json.encodeToString(dto))
                    }
                ) {
                    Assertions.assertEquals(HttpStatusCode.Unauthorized, response.status())
                }
            }
        }
    }

    @Test
    fun `Test Create User Success`() {
        testApplication {
            transaction {
                val pw = "Password"
                val login = "Login"
                val dto: UserCreationDto = UserCreationDto(
                    login = login,
                    password = pw
                )

                with(
                    handleRequest(HttpMethod.Post, "/user") {
                        addHeader("Content-Type", "application/json")
                        setBody(Json.encodeToString(dto))
                    }
                ) {
                    Assertions.assertEquals(HttpStatusCode.NoContent, response.status())
                }
            }
        }
    }

    @Test
    fun `Test Create User With Same Login Failure`() {
        testApplication {
            transaction {

                val user = createUser("Login")
                val pw = "Password"
                val dto: UserCreationDto = UserCreationDto(
                    login = user.login,
                    password = pw
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
