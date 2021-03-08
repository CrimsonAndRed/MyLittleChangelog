package my.little.changelog.routing

import io.ktor.config.*
import io.ktor.http.*
import io.ktor.routing.*
import io.ktor.server.testing.*
import io.ktor.util.*
import io.mockk.MockKMatcherScope
import io.mockk.every
import kotlinx.serialization.encodeToString
import my.little.changelog.configuration.Json
import my.little.changelog.configuration.auth.JwtConfig
import my.little.changelog.model.auth.User
import my.little.changelog.model.auth.Users
import my.little.changelog.module
import org.jetbrains.exposed.dao.id.EntityID
import kotlin.test.assertEquals

@KtorExperimentalAPI
abstract class AbstractRouterTest(
    private val routing: Routing.() -> Unit
) {

    protected fun testApplication(test: TestApplicationEngine.() -> Unit) {
        withTestApplication(
            {
                (environment.config as MapApplicationConfig).apply {
                    put("jwt.audience", "test")
                    put("jwt.issuer", "test")
                    put("jwt.subject", "test")
                    put("jwt.secret", "test")
                    put("jwt.realm", "test")
                }
                module(testing = true, authTest = true)
                routing(routing)

                // TODO Why this does not work?

//                uninstall(Authentication.Feature)
//
//                install(Authentication) {
//                    jwt {
//
//                        JwtConfig.audience = environment.config.property("jwt.audience").getString()
//                        JwtConfig.issuer = environment.config.property("jwt.issuer").getString()
//                        JwtConfig.subject = environment.config.property("jwt.subject").getString()
//                        JwtConfig.secret = environment.config.property("jwt.secret").getString()
//                        realm = environment.config.property("jwt.realm").getString()
//                        verifier(JwtConfig.generateVerifier())
//                        validate { credential ->
//                            CustomPrincipal(credential.payload, getTestUser())
//                        }
//                    }
//                }
            },
            test
        )
    }

    public fun getTestUser(): User {
        return User(EntityID(0, Users))
    }

    protected fun testRoute(method: HttpMethod, uri: String, expectation: TestApplicationCall.() -> Unit) = testApplication {
        with(constructRequest(method, uri)(), expectation)
    }

    protected inline fun <reified T> testRoute(
        method: HttpMethod,
        uri: String,
        dto: T,
        contentType: ContentType = ContentType.Application.Json,
        noinline expectation: TestApplicationCall.() -> Unit
    ) = testApplication {
        with(constructRequest(method, uri, dto, contentType)(), expectation)
    }

    protected fun testAuthorizedRoute(method: HttpMethod, uri: String, expectation: TestApplicationCall.() -> Unit) = testApplication {
        with(constructAuthorizedRequest(method, uri)(), expectation)
    }

    protected inline fun <reified T> testAuthorizedRoute(
        method: HttpMethod,
        uri: String,
        dto: T,
        contentType: ContentType = ContentType.Application.Json,
        noinline expectation: TestApplicationCall.() -> Unit
    ) = testApplication {
        with(constructAuthorizedRequest(method, uri, dto, contentType)(), expectation)
    }

    protected fun constructRequest(method: HttpMethod, uri: String): TestApplicationEngine.() -> TestApplicationCall = {
        handleRequest(method, uri)
    }

    protected inline fun <reified T> constructRequest(method: HttpMethod, uri: String, dto: T, contentType: ContentType = ContentType.Application.Json): TestApplicationEngine.() -> TestApplicationCall = {
        handleRequest(method, uri) {
            addHeader("Content-Type", contentType.toString())
            setBody(Json.encodeToString(dto))
        }
    }

    protected fun constructAuthorizedRequest(method: HttpMethod, uri: String): TestApplicationEngine.() -> TestApplicationCall = {
        handleRequest(method, uri) {
            addHeader("Authorization", "Bearer ${JwtConfig.makeToken(getTestUser())}")
        }
    }

    protected inline fun <reified T> constructAuthorizedRequest(method: HttpMethod, uri: String, dto: T, contentType: ContentType = ContentType.Application.Json): TestApplicationEngine.() -> TestApplicationCall = {
        handleRequest(method, uri) {
            addHeader("Authorization", "Bearer ${JwtConfig.makeToken(getTestUser())}")
            addHeader("Content-Type", contentType.toString())
            setBody(Json.encodeToString(dto))
        }
    }

    protected fun testExceptions(
        applicationCall: TestApplicationEngine.() -> TestApplicationCall,
        mocks: List<MockKMatcherScope.() -> Any>,
        exceptions: List<Pair<() -> Exception, HttpStatusCode>>
    ) {
        exceptions.forEach { ex ->
            mocks.forEach { mock ->
                every(mock) throws ex.first()
            }

            testApplication {
                with(applicationCall()) {
                    assertEquals(ex.second, response.status())
                }
            }
        }
    }

    protected fun testAuthorizedExceptions(
        applicationCall: TestApplicationEngine.() -> TestApplicationCall,
        mocks: List<MockKMatcherScope.() -> Any>,
        exceptions: List<Pair<() -> Exception, HttpStatusCode>>
    ) {
        exceptions.forEach { ex ->
            mocks.forEach { mock ->
                every(mock) throws ex.first()
            }

            testApplication {
                with(applicationCall()) {
                    assertEquals(ex.second, response.status())
                }
            }
        }
    }
}
