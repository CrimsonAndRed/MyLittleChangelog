package my.little.changelog.routing.version.group

import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.TestApplicationEngine
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.setBody
import io.ktor.util.KtorExperimentalAPI
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import my.little.changelog.configuration.Json
import my.little.changelog.model.group.Group
import my.little.changelog.model.group.dto.external.ChangeGroupPositionDto
import my.little.changelog.model.group.dto.external.GroupCreationDto
import my.little.changelog.model.group.dto.external.GroupUpdateDto
import my.little.changelog.model.group.dto.external.ReturnedGroupDto
import my.little.changelog.model.leaf.Leaf
import my.little.changelog.model.version.Version
import my.little.changelog.persistence.repo.GroupRepo
import my.little.changelog.routing.AbstractIntegrationTest
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.test.assertNotEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

@KtorExperimentalAPI
internal class GroupIntegrationTest : AbstractIntegrationTest() {

    @Test
    fun `Test Root Group Creation Success`() {
        testApplication {
            transaction {
                val version = createVersion()
                val dto = GroupCreationDto("Тестовая Группа")

                with(
                    handleRequest(HttpMethod.Post, "version/${version.id.value}/group") {
                        addHeader("Content-Type", "application/json")
                        setBody(Json.encodeToString(dto))
                    }
                ) {
                    assertEquals(HttpStatusCode.OK, response.status())

                    val response: ReturnedGroupDto = Json.decodeFromString(response.content!!)
                    assertEquals(dto.name, response.name)
                    assertNull(response.parentVid)
                }
            }
        }
    }

    @Test
    fun `Test Subgroup Creation Success`() {
        testApplication {
            transaction {
                val version = createVersion()
                val group = createGroup(version)
                val dto = GroupCreationDto("Тестовая Группа", parentVid = group.vid)

                with(
                    handleRequest(HttpMethod.Post, "version/${version.id.value}/group") {
                        addHeader("Content-Type", "application/json")
                        setBody(Json.encodeToString(dto))
                    }
                ) {
                    assertEquals(HttpStatusCode.OK, response.status())

                    val response: ReturnedGroupDto = Json.decodeFromString(response.content!!)
                    assertEquals(dto.name, response.name)
                    assertEquals(dto.parentVid, response.parentVid)
                }
            }
        }
    }

    @Test
    fun `Test Group Append to New Version`() {
        testApplication {
            transaction {
                val version1 = createVersion()
                val version2 = createVersion()
                val group = createGroup(version1)
                val dto = GroupCreationDto(group.name, vid = group.vid)

                with(
                    handleRequest(HttpMethod.Post, "version/${version2.id.value}/group") {
                        addHeader("Content-Type", "application/json")
                        setBody(Json.encodeToString(dto))
                    }
                ) {
                    assertEquals(HttpStatusCode.OK, response.status())

                    val response: ReturnedGroupDto = Json.decodeFromString(response.content!!)
                    assertEquals(dto.name, response.name)
                    assertEquals(dto.parentVid, response.parentVid)
                    assertEquals(dto.vid, response.vid)
                    assertNotEquals(group.id.value, response.id)
                }
            }
        }
    }

    @Test
    fun `Test Group Create With Nonexistent Version`() {
        testApplication {
            transaction {
                val version1 = createVersion()
                val version2 = createVersion()
                val group = createGroup(version1)
                val dto = GroupCreationDto(group.name, vid = group.vid)

                with(
                    handleRequest(HttpMethod.Post, "version/${version2.id.value + 1}/group") {
                        addHeader("Content-Type", "application/json")
                        setBody(Json.encodeToString(dto))
                    }
                ) {
                    assertEquals(HttpStatusCode.InternalServerError, response.status())
                }
            }
        }
    }

    @Test
    fun `Test Group Create With Nonexistent Parent Group Failure`() {
        testApplication {
            transaction {
                val version1 = createVersion()
                val version2 = createVersion()
                val group = createGroup(version1)
                val dto = GroupCreationDto(group.name, vid = group.vid + 1)

                with(
                    handleRequest(HttpMethod.Post, "version/$version2/group") {
                        addHeader("Content-Type", "application/json")
                        setBody(Json.encodeToString(dto))
                    }
                ) {
                    assertEquals(HttpStatusCode.InternalServerError, response.status())
                }
            }
        }
    }

    @Test
    fun `Test Group Update Name Success`() {
        testApplication {
            transaction {
                val version = createVersion()
                val group = createGroup(version)
                val dto = GroupUpdateDto("Тестовая Базовая Группа 2", null)

                with(
                    handleRequest(HttpMethod.Put, "version/${version.id.value}/group/${group.id.value}") {
                        addHeader("Content-Type", "application/json")
                        setBody(Json.encodeToString(dto))
                    }
                ) {
                    assertEquals(HttpStatusCode.NoContent, response.status())
                }
            }
        }
    }

    @Test
    fun `Test Group Update Parent Success`() {
        testApplication {
            transaction {
                val version = createVersion()
                val groupRoot = createGroup(version)
                val groupSub = createGroup(version)

                val dto = GroupUpdateDto("Тестовая Базовая Группа 2", groupRoot.id.value)

                with(
                    handleRequest(HttpMethod.Put, "version/${version.id.value}/group/${groupSub.id.value}") {
                        addHeader("Content-Type", "application/json")
                        setBody(Json.encodeToString(dto))
                    }
                ) {
                    assertEquals(HttpStatusCode.NoContent, response.status())
                }
            }
        }
    }

    @Test
    fun `Test Group Update With Old Version Failure`() {
        testApplication {
            transaction {
                val version = createVersion()
                createVersion()
                val group = createGroup(version)
                val dto = GroupUpdateDto("Тестовая Базовая Группа 2", null)

                with(
                    handleRequest(HttpMethod.Put, "version/${version.id.value}/group/${group.id.value}") {
                        addHeader("Content-Type", "application/json")
                        setBody(Json.encodeToString(dto))
                    }
                ) {
                    assertEquals(HttpStatusCode.BadRequest, response.status())
                    val response = Json.decodeFromString<List<String>>(response.content!!)
                    assertTrue { 1 >= response.size }
                }
            }
        }
    }

    @Test
    fun `Test Group Delete Success`() {
        testApplication {
            transaction {
                val version = createVersion()
                val group = createGroup(version)
                with(
                    handleRequest(HttpMethod.Delete, "version/${version.id.value}/group/${group.id.value}?hierarchy=true") {
                        addHeader("Content-Type", "application/json")
                    }
                ) {
                    assertEquals(HttpStatusCode.NoContent, response.status())
                }
            }
        }
    }

    @Test
    fun `Test Group Delete With Old Version Failure`() {
        testApplication {
            transaction {
                val version = createVersion()
                val version2 = createVersion()
                val group = createGroup(version)

                with(
                    handleRequest(HttpMethod.Delete, "version/${version2.id.value}/group/${group.id.value}?hierarchy=true") {
                        addHeader("Content-Type", "application/json")
                    }
                ) {
                    assertEquals(HttpStatusCode.BadRequest, response.status())
                    val response = Json.decodeFromString<List<String>>(response.content!!)
                    assertTrue { 1 >= response.size }
                }
            }
        }
    }

    @Test
    fun `Test Group Dematerialize Sublatest Failure`() {
        testApplication {
            transaction {
                val version = createVersion()
                val group = createGroup(version)
                createLeaf(version, group.vid)
                with(
                    handleRequest(HttpMethod.Delete, "version/${version.id.value}/group/${group.id.value}?hierarchy=false") {
                        addHeader("Content-Type", "application/json")
                    }
                ) {
                    assertEquals(HttpStatusCode.BadRequest, response.status())
                }
            }
        }
    }

    @Test
    fun `Test Group Delete Hierarchy Success`() {
        testApplication {
            transaction {
                val version = createVersion()
                val group = createGroup(version)
                val leaf = createLeaf(version, group.vid)
                with(
                    handleRequest(HttpMethod.Delete, "version/${version.id.value}/group/${group.id.value}?hierarchy=true") {
                        addHeader("Content-Type", "application/json")
                    }
                ) {
                    assertEquals(HttpStatusCode.NoContent, response.status())
                }

                assertNull(Leaf.findById(leaf.id.value))
            }
        }
    }

    @Test
    fun `Test Group Delete With Nonexistent Version Success`() {
        testApplication {
            transaction {
                val version = createVersion()
                val group = createGroup(version)
                with(
                    handleRequest(HttpMethod.Delete, "version/${version.id.value + 1}/group/${group.id.value}?hierarchy=true") {
                        addHeader("Content-Type", "application/json")
                    }
                ) {
                    assertEquals(HttpStatusCode.NoContent, response.status())
                }
            }
        }
    }

    @Test
    fun `Test Group Delete With Nonexistent Group`() {
        testApplication {
            transaction {
                val version = createVersion()
                val group = createGroup(version)
                with(
                    handleRequest(HttpMethod.Delete, "version/${version.id.value}/group/${group.id.value + 1}?hierarchy=true") {
                        addHeader("Content-Type", "application/json")
                    }
                ) {
                    assertEquals(HttpStatusCode.InternalServerError, response.status())
                }
            }
        }
    }

    @Test
    fun `Test Move group`() {
        testApplication {
            transaction {
                val version = createVersion()
                val parentGroup = createGroup(version)
                val group1 = createGroup(version, parentGroup.vid)
                val group2 = createGroup(version, parentGroup.vid)
                testMoveGroupPositive(version, group1, group2)
            }
            transaction {
                val version = createVersion()
                val parentGroup = createGroup(version)
                val group1 = createGroup(version, parentGroup.vid)
                val group2 = createGroup(version, parentGroup.vid)
                testMoveGroupPositive(version, group2, group1)
            }
            transaction {
                val version = createVersion()
                val parentGroup1 = createGroup(version)
                val parentGroup2 = createGroup(version)
                val group1 = createGroup(version, parentGroup1.vid)
                val group2 = createGroup(version, parentGroup2.vid)
                testMoveGroupNegative(version, group1, group2)
            }
        }
    }

    private fun TestApplicationEngine.testMoveGroupPositive(version: Version, group1: Group, group2: Group) {
        val dto = ChangeGroupPositionDto(group2.id.value)
        handleRequest(HttpMethod.Patch, "/version/${version.id}/group/${group1.id}/position") {
            addHeader("Content-Type", "application/json")
            setBody(Json.encodeToString(dto))
        }.apply {
            assertEquals(HttpStatusCode.NoContent, response.status())
            assertEquals(GroupRepo.findById(group1.id.value).order, group2.order)
            assertEquals(GroupRepo.findById(group2.id.value).order, group1.order)
        }
    }

    private fun TestApplicationEngine.testMoveGroupNegative(version: Version, group1: Group, group2: Group) {
        val dto = ChangeGroupPositionDto(group2.id.value)
        handleRequest(HttpMethod.Patch, "/version/${version.id}/group/${group1.id}/position") {
            addHeader("Content-Type", "application/json")
            setBody(Json.encodeToString(dto))
        }.apply {
            assertEquals(HttpStatusCode.BadRequest, response.status())
        }
    }
}
