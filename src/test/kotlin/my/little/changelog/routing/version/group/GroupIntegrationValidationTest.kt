package my.little.changelog.routing.version.group

import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.setBody
import io.ktor.util.KtorExperimentalAPI
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import my.little.changelog.configuration.Json
import my.little.changelog.model.group.dto.external.GroupCreationDto
import my.little.changelog.model.group.dto.external.GroupUpdateDto
import my.little.changelog.routing.AbstractIntegrationTest
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

@KtorExperimentalAPI
internal class GroupIntegrationValidationTest : AbstractIntegrationTest() {
    @Test
    fun `Test Group Create With Blank Name`() {
        testApplication {
            transaction {
                val version1 = createVersion()
                val dto = GroupCreationDto(" ")

                with(
                    handleRequest(HttpMethod.Post, "version/${version1.id.value}/group") {
                        addHeader("Content-Type", "application/json")
                        setBody(Json.encodeToString(dto))
                    }
                ) {
                    Assertions.assertEquals(HttpStatusCode.BadRequest, response.status())
                    val response = Json.decodeFromString<List<String>>(response.content!!)
                    assertTrue { 1 >= response.size }
                }
            }
        }
    }

    @Test
    fun `Test Group Update With Blank Name`() {
        testApplication {
            transaction {
                val version1 = createVersion()
                val group = createGroup(version1)
                val dto = GroupUpdateDto(" ")

                with(
                    handleRequest(HttpMethod.Put, "version/${version1.id.value}/group/${group.id.value}") {
                        addHeader("Content-Type", "application/json")
                        setBody(Json.encodeToString(dto))
                    }
                ) {
                    Assertions.assertEquals(HttpStatusCode.BadRequest, response.status())
                    val response = Json.decodeFromString<List<String>>(response.content!!)
                    assertTrue { 1 >= response.size }
                }
            }
        }
    }
}
