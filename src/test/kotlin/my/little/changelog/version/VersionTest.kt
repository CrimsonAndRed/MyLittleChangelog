package my.little.changelog.version

import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.handleRequest
import io.ktor.util.KtorExperimentalAPI
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

@KtorExperimentalAPI
class VersionTest : AbstractIntegrationTest() {

    @Test
    fun test1() {
        testApplication {
            with(handleRequest(HttpMethod.Get, "/version?id=1")) {
                assertEquals(HttpStatusCode.OK, response.status())
            }
        }
    }
}