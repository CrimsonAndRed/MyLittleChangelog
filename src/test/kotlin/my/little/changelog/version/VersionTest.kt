package my.little.changelog.version

import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.handleRequest
import io.ktor.util.KtorExperimentalAPI
import my.little.changelog.model.version.Version
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

@KtorExperimentalAPI
class VersionTest : AbstractIntegrationTest() {

    @Test
    fun test1() {
        testApplication {
            val version = transaction {
                Version.new {}
            }

            with(handleRequest(HttpMethod.Get, "/version?id=${version.id}")) {
                assertEquals(HttpStatusCode.OK, response.status())
            }
        }
    }
}
