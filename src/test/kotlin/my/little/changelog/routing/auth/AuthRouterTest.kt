package my.little.changelog.routing.auth

import io.ktor.http.*
import io.ktor.util.*
import io.mockk.every
import io.mockk.mockkObject
import my.little.changelog.exception.ForbiddenException
import my.little.changelog.exception.UnauthException
import my.little.changelog.model.auth.dto.external.AuthDto
import my.little.changelog.model.auth.dto.external.UserCreationDto
import my.little.changelog.routing.AbstractRouterTest
import my.little.changelog.service.auth.AuthService
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

@KtorExperimentalAPI
class AuthRouterTest : AbstractRouterTest(
    { authRouting() }
) {

    init {
        mockkObject(AuthService)
    }

    private val authBaseUrl: String = "/auth"
    private val userBaseUrl: String = "/user"

    @Test
    fun `Test Auth Success`() {
        val token = "token.token.token"
        val dto: AuthDto = AuthDto(
            login = "Test Login",
            password = "Test password"
        )

        every { AuthService.auth(allAny()) } returns token

        testRoute(HttpMethod.Post, authBaseUrl, dto) {
            assertEquals(HttpStatusCode.OK, response.status())
        }
    }

    @Test
    fun `Test Auth Exception`() {
        val dto: AuthDto = AuthDto(
            login = "Test Login",
            password = "Test password"
        )
        testExceptions(
            constructRequest(HttpMethod.Post, authBaseUrl, dto),
            listOf { AuthService.auth(allAny()) },
            listOf(
                { RuntimeException() } to HttpStatusCode.InternalServerError,
                { UnauthException() } to HttpStatusCode.Unauthorized,
                { ForbiddenException() } to HttpStatusCode.Forbidden,
            )
        )
    }

    @Test
    fun `Test New User Success`() {
        val dto: UserCreationDto = UserCreationDto(
            login = "Test Login",
            password = "Test password"
        )

        every { AuthService.newUser(allAny()) } returns Unit

        testRoute(HttpMethod.Post, userBaseUrl, dto) {
            assertEquals(HttpStatusCode.NoContent, response.status())
        }
    }

    @Test
    fun `Test New User Exception`() {
        val dto: UserCreationDto = UserCreationDto(
            login = "Test Login",
            password = "Test password"
        )
        testExceptions(
            constructRequest(HttpMethod.Post, userBaseUrl, dto),
            listOf { AuthService.newUser(allAny()) },
            listOf(
                { RuntimeException() } to HttpStatusCode.InternalServerError,
                { UnauthException() } to HttpStatusCode.Unauthorized,
                { ForbiddenException() } to HttpStatusCode.Forbidden,
            )
        )
    }
}
