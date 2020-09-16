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
import my.little.changelog.model.group.dto.external.GroupUpdateDto
import my.little.changelog.model.group.dto.external.ReturnedGroupDto
import my.little.changelog.model.version.Version
import my.little.changelog.routing.AbstractIntegrationTest
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.test.assertNotEquals
import kotlin.test.assertNull

@KtorExperimentalAPI
internal class GroupIntegrationTest : AbstractIntegrationTest() {

    @Test
    fun `Test Root Group Creation Success`() {
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

                val response: ReturnedGroupDto = Json.decodeFromString(response.content!!)
                assertEquals(dto.name, response.name)
                assertNull(response.parentId)
            }
        }
    }

    @Test
    fun `Test Subgroup Creation Success`() {
        testApplication {
            val (version, group) = transaction {
                val version = Version.new { }
                val group = Group.new {
                    this.version = version
                    this.name = "Тестовая Базовая Группа"
                }
                version to group
            }

            val dto = GroupCreationDto("Тестовая Группа", parentId = group.id.value)

            with(
                handleRequest(HttpMethod.Post, "/version/${version.id}/group") {
                    addHeader("Content-Type", "application/json")
                    setBody(Json.encodeToString(dto))
                }
            ) {
                assertEquals(HttpStatusCode.OK, response.status())

                val response: ReturnedGroupDto = Json.decodeFromString(response.content!!)
                assertEquals(dto.name, response.name)
                assertEquals(dto.parentId, response.parentId)
            }
        }
    }

    @Test
    fun `Test Group Append to New Version`() {
        testApplication {
            transaction {
                val version1 = Version.new { }
                val version2 = Version.new { }
                val group = Group.new {
                    this.version = version1
                    this.name = "Тестовая Группа"
                }
                commit()

                val dto = GroupCreationDto(group.name, vid = group.vid)

                with(
                    handleRequest(HttpMethod.Post, "/version/${version2.id}/group") {
                        addHeader("Content-Type", "application/json")
                        setBody(Json.encodeToString(dto))
                    }
                ) {
                    assertEquals(HttpStatusCode.OK, response.status())

                    val response: ReturnedGroupDto = Json.decodeFromString(response.content!!)
                    assertEquals(dto.name, response.name)
                    assertEquals(dto.parentId, response.parentId)
                    assertEquals(dto.vid, response.vid)
                    assertNotEquals(group.id.value, response.id)
                }
            }
        }
    }

    @Test
    fun `Test Group Update Name`() {
        testApplication {
            transaction {
                val version = Version.new { }
                val group = Group.new {
                    this.version = version
                    this.name = "Тестовая Базовая Группа"
                }

                commit()

                val dto = GroupUpdateDto("Тестовая Базовая Группа 2", null)

                with(
                    handleRequest(HttpMethod.Put, "/version/${version.id}/group/${group.id}") {
                        addHeader("Content-Type", "application/json")
                        setBody(Json.encodeToString(dto))
                    }
                ) {
                    assertEquals(HttpStatusCode.OK, response.status())

                    val response: ReturnedGroupDto = Json.decodeFromString(response.content!!)
                    assertEquals(group.id.value, response.id)
                    assertEquals(group.vid, response.vid)
                    assertEquals(dto.name, response.name)
                    assertEquals(dto.parentId, response.parentId)
                }
            }
        }
    }

    @Test
    fun `Test Group Update Parent`() {
        testApplication {
            transaction {
                val version = Version.new { }
                val groupRoot = Group.new {
                    this.version = version
                    this.name = "Тестовая Базовая Группа"
                }

                val groupSub = Group.new {
                    this.version = version
                    this.name = "Тестовая Нижняя Группа"
                }

                commit()

                val dto = GroupUpdateDto("Тестовая Базовая Группа 2", groupRoot.id.value)

                with(
                    handleRequest(HttpMethod.Put, "/version/${version.id}/group/${groupSub.id}") {
                        addHeader("Content-Type", "application/json")
                        setBody(Json.encodeToString(dto))
                    }
                ) {
                    assertEquals(HttpStatusCode.OK, response.status())

                    val response: ReturnedGroupDto = Json.decodeFromString(response.content!!)
                    assertEquals(groupSub.id.value, response.id)
                    assertEquals(groupSub.vid, response.vid)
                    assertEquals(dto.name, response.name)
                    assertEquals(dto.parentId, response.parentId)
                }
            }
        }
    }
}
