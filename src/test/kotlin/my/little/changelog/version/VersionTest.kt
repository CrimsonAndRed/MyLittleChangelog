package my.little.changelog.version

import io.ktor.config.MapApplicationConfig
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.withTestApplication
import io.ktor.util.KtorExperimentalAPI
import my.little.changelog.module as applicationModule
import my.little.changelog.routing.module as routingModule
import my.little.changelog.persistence.module as persistenceModule

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

@KtorExperimentalAPI
class VersionTest {

    @Test
    fun test1() {
        withTestApplication({
            (environment.config as MapApplicationConfig).apply {
                put("database.url", "jdbc:postgresql://localhost:5432/postgres")
                put("database.username", "postgres")
                put("database.password", "postgres")
            }
            applicationModule(testing = true)
            routingModule()
            persistenceModule()
        }) {
            with(handleRequest(HttpMethod.Get, "/version?id=2")) {
                assertEquals(HttpStatusCode.OK, response.status())
            }
        }
    }
}