package my.little.changelog.routing.version.group

import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.setBody
import io.ktor.util.KtorExperimentalAPI
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import my.little.changelog.configuration.Json
import my.little.changelog.model.group.Group
import my.little.changelog.model.group.dto.external.GroupCreationDto
import my.little.changelog.model.group.dto.external.NewGroupDto
import my.little.changelog.model.version.Version
import my.little.changelog.routing.AbstractIntegrationTest
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.test.assertNull

@KtorExperimentalAPI
internal class GroupIntegrationTest : AbstractIntegrationTest() {

    @Test
    fun testRootGroupCreation() {
        testApplication {
            val version = transaction {
                Version.new { }
            }

            val dto = GroupCreationDto("Тестовая Группа")

            with(
                handleRequest(HttpMethod.Post, "/version/${version.id}/group") {
                    addHeader("Content-Type", "application/json")
                    setBody(Json.encodeToString(dto))
                }
            ) {
                assertEquals(HttpStatusCode.OK, response.status())

                val response: NewGroupDto = Json.decodeFromString(response.content!!)
                assertEquals(dto.name, response.name)
                assertNull(response.parentId)
            }
        }
    }

    @Test
    fun testSubGroupCreation() {
        testApplication {
            val (version, group) = transaction {
                val version = Version.new { }
                val group = Group.new {
                    this.version = version
                    this.name = "Тестовая Базовая Группа"
                }
                version to group
            }

            val dto = GroupCreationDto("Тестовая Группа", group.id.value)

            with(
                handleRequest(HttpMethod.Post, "/version/${version.id}/group") {
                    addHeader("Content-Type", "application/json")
                    setBody(Json.encodeToString(dto))
                }
            ) {
                assertEquals(HttpStatusCode.OK, response.status())

                val response: NewGroupDto = Json.decodeFromString(response.content!!)
                assertEquals(dto.name, response.name)
                assertEquals(dto.parentId, response.parentId)
            }
        }
    }
}
