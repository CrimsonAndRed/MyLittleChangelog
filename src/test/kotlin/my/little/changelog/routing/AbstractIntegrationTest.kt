package my.little.changelog.routing

import io.ktor.config.MapApplicationConfig
import io.ktor.server.testing.TestApplicationEngine
import io.ktor.server.testing.withTestApplication
import io.ktor.util.KtorExperimentalAPI
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import my.little.changelog.module as applicationModule
import my.little.changelog.persistence.module as persistenceModule
import my.little.changelog.routing.module as routingModule

@KtorExperimentalAPI
@Testcontainers
abstract class AbstractIntegrationTest {
    companion object Static {
        @Container
        val postgres = PostgreSQLContainer<Nothing>("postgres:13")
    }

    protected fun testApplication(test: TestApplicationEngine.() -> Unit) {
        withTestApplication(
            {
                (environment.config as MapApplicationConfig).apply {
                    put("database.url", postgres.jdbcUrl)
                    put("database.username", postgres.username)
                    put("database.password", postgres.password)
                }
                applicationModule(testing = true)
                routingModule()
                persistenceModule()
            },
            test
        )
    }
}
