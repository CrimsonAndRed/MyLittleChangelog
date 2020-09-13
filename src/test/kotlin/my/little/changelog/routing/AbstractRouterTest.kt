package my.little.changelog.routing

import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.routing.Routing
import io.ktor.routing.routing
import io.ktor.server.testing.TestApplicationCall
import io.ktor.server.testing.TestApplicationEngine
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.setBody
import io.ktor.server.testing.withTestApplication
import io.ktor.util.KtorExperimentalAPI
import kotlinx.serialization.encodeToString
import my.little.changelog.configuration.Json
import my.little.changelog.module

@KtorExperimentalAPI
abstract class AbstractRouterTest(
    private val routing: Routing.() -> Unit
) {

    protected fun testApplication(test: TestApplicationEngine.() -> Unit) {
        withTestApplication(
            {
                module(true)
                routing(routing)
            },
            test
        )
    }

    protected fun testRoute(method: HttpMethod, uri: String, expectation: TestApplicationCall.() -> Unit) = testApplication {
        with(handleRequest(method, uri), expectation)
    }

    protected inline fun <reified T> testRoute(
        method: HttpMethod,
        uri: String,
        dto: T,
        contentType: ContentType = ContentType.Application.Json,
        noinline expectation: TestApplicationCall.() -> Unit
    ) =
        testApplication {
            with(
                handleRequest(method, uri) {
                    addHeader("Content-Type", contentType.toString())
                    setBody(Json.encodeToString(dto))
                },
                expectation
            )
        }
}
