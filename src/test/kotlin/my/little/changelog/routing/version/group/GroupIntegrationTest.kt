package my.little.changelog.routing.version.group

import io.ktor.http.*
import io.ktor.server.testing.*
import io.ktor.util.*
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
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.test.assertNotEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

@KtorExperimentalAPI
internal class GroupIntegrationTest : AbstractIntegrationTest() {

    @Test
    fun `Test Root Group Creation Success`() {
        authorizedTest { user, token, transaction ->
            val version = transaction.createVersion(user)
            val dto = GroupCreationDto("Test Name 1")

            with(
                handleRequest(HttpMethod.Post, "version/${version.id.value}/group") {
                    addHeader("Content-Type", "application/json")
                    addHeader("Authorization", "Bearer $token")
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

    @Test
    fun `Test Subgroup Creation Success`() {
        authorizedTest { user, token, transaction ->
            val version = transaction.createVersion(user)
            val group = transaction.createGroup(version)
            val dto = GroupCreationDto("Test Name 1", parentVid = group.vid)

            with(
                handleRequest(HttpMethod.Post, "version/${version.id.value}/group") {
                    addHeader("Authorization", "Bearer $token")
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

    @Test
    fun `Test Group Append to New Version Success`() {
        authorizedTest { user, token, transaction ->
            val version1 = transaction.createVersion(user)
            val version2 = transaction.createVersion(user)
            val group = transaction.createGroup(version1)
            val dto = GroupCreationDto(group.name, vid = group.vid)

            with(
                handleRequest(HttpMethod.Post, "version/${version2.id.value}/group") {
                    addHeader("Authorization", "Bearer $token")
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

    @Test
    fun `Test Group Create With Nonexistent Version Failure`() {
        authorizedTest { user, token, transaction ->
            val version1 = transaction.createVersion(user)
            val version2 = transaction.createVersion(user)
            val group = transaction.createGroup(version1)
            val dto = GroupCreationDto(group.name, vid = group.vid)

            with(
                handleRequest(HttpMethod.Post, "version/${version2.id.value + 1}/group") {
                    addHeader("Authorization", "Bearer $token")
                    addHeader("Content-Type", "application/json")
                    setBody(Json.encodeToString(dto))
                }
            ) {
                assertEquals(HttpStatusCode.InternalServerError, response.status())
            }
        }
    }

    @Test
    fun `Test Group Create With Nonexistent Parent Group Failure`() {
        authorizedTest { user, token, transaction ->
            val version1 = transaction.createVersion(user)
            val version2 = transaction.createVersion(user)
            val group = transaction.createGroup(version1)
            val dto = GroupCreationDto(group.name, vid = group.vid + 1)

            with(
                handleRequest(HttpMethod.Post, "version/$version2/group") {
                    addHeader("Authorization", "Bearer $token")
                    addHeader("Content-Type", "application/json")
                    setBody(Json.encodeToString(dto))
                }
            ) {
                assertEquals(HttpStatusCode.InternalServerError, response.status())
            }
        }
    }

    @Test
    fun `Test Group Update Name Success`() {
        authorizedTest { user, token, transaction ->
            val version = transaction.createVersion(user)
            val group = transaction.createGroup(version)
            val dto = GroupUpdateDto("Test Base Group 2", null)

            with(
                handleRequest(HttpMethod.Put, "version/${version.id.value}/group/${group.id.value}") {
                    addHeader("Authorization", "Bearer $token")
                    addHeader("Content-Type", "application/json")
                    setBody(Json.encodeToString(dto))
                }
            ) {
                assertEquals(HttpStatusCode.NoContent, response.status())
            }
        }
    }

    @Test
    fun `Test Group Update Parent Success`() {
        authorizedTest { user, token, transaction ->
            val version = transaction.createVersion(user)
            val groupRoot = transaction.createGroup(version)
            val groupSub = transaction.createGroup(version)

            val dto = GroupUpdateDto("Test Base Group 2", groupRoot.id.value)

            with(
                handleRequest(HttpMethod.Put, "version/${version.id.value}/group/${groupSub.id.value}") {
                    addHeader("Authorization", "Bearer $token")
                    addHeader("Content-Type", "application/json")
                    setBody(Json.encodeToString(dto))
                }
            ) {
                assertEquals(HttpStatusCode.NoContent, response.status())
            }
        }
    }

    @Test
    fun `Test Group Update With Old Version Failure`() {
        authorizedTest { user, token, transaction ->
            val version = transaction.createVersion(user)
            transaction.createVersion(user)
            val group = transaction.createGroup(version)
            val dto = GroupUpdateDto("Test Base Group 2", null)

            with(
                handleRequest(HttpMethod.Put, "version/${version.id.value}/group/${group.id.value}") {
                    addHeader("Authorization", "Bearer $token")
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

    @Test
    fun `Test Group Delete Success`() {
        authorizedTest { user, token, transaction ->
            val version = transaction.createVersion(user)
            val group = transaction.createGroup(version)
            with(
                handleRequest(
                    HttpMethod.Delete,
                    "version/${version.id.value}/group/${group.id.value}?hierarchy=true"
                ) {
                    addHeader("Authorization", "Bearer $token")
                    addHeader("Content-Type", "application/json")
                }
            ) {
                assertEquals(HttpStatusCode.NoContent, response.status())
            }
        }
    }

    @Test
    fun `Test Group Delete With Old Version Failure`() {
        authorizedTest { user, token, transaction ->
            val version = transaction.createVersion(user)
            val version2 = transaction.createVersion(user)
            val group = transaction.createGroup(version)

            with(
                handleRequest(
                    HttpMethod.Delete,
                    "version/${version2.id.value}/group/${group.id.value}?hierarchy=true"
                ) {
                    addHeader("Authorization", "Bearer $token")
                    addHeader("Content-Type", "application/json")
                }
            ) {
                assertEquals(HttpStatusCode.BadRequest, response.status())
                val response = Json.decodeFromString<List<String>>(response.content!!)
                assertTrue { 1 >= response.size }
            }
        }
    }

    @Test
    fun `Test Group Dematerialize Sublatest Failure`() {
        authorizedTest { user, token, transaction ->
            val version = transaction.createVersion(user)
            val group = transaction.createGroup(version)
            transaction.createLeaf(version, group.vid)
            with(
                handleRequest(
                    HttpMethod.Delete,
                    "version/${version.id.value}/group/${group.id.value}?hierarchy=false"
                ) {
                    addHeader("Authorization", "Bearer $token")
                    addHeader("Content-Type", "application/json")
                }
            ) {
                assertEquals(HttpStatusCode.BadRequest, response.status())
            }
        }
    }

    @Test
    fun `Test Group Delete Hierarchy Success`() {
        authorizedTest { user, token, transaction ->
            val version = transaction.createVersion(user)
            val group = transaction.createGroup(version)
            val leaf = transaction.createLeaf(version, group.vid)
            with(
                handleRequest(
                    HttpMethod.Delete,
                    "version/${version.id.value}/group/${group.id.value}?hierarchy=true"
                ) {
                    addHeader("Authorization", "Bearer $token")
                    addHeader("Content-Type", "application/json")
                }
            ) {
                assertEquals(HttpStatusCode.NoContent, response.status())
            }

            assertNull(Leaf.findById(leaf.id.value))
        }
    }

    @Test
    fun `Test Group Delete With Nonexistent Version Success`() {
        authorizedTest { user, token, transaction ->
            val version = transaction.createVersion(user)
            val group = transaction.createGroup(version)
            with(
                handleRequest(
                    HttpMethod.Delete,
                    "version/${version.id.value + 1}/group/${group.id.value}?hierarchy=true"
                ) {
                    addHeader("Authorization", "Bearer $token")
                    addHeader("Content-Type", "application/json")
                }
            ) {
                assertEquals(HttpStatusCode.NoContent, response.status())
            }
        }
    }

    @Test
    fun `Test Group Delete With Nonexistent Group Failure`() {
        authorizedTest { user, token, transaction ->
            val version = transaction.createVersion(user)
            val group = transaction.createGroup(version)
            with(
                handleRequest(
                    HttpMethod.Delete,
                    "version/${version.id.value}/group/${group.id.value + 1}?hierarchy=true"
                ) {
                    addHeader("Authorization", "Bearer $token")
                    addHeader("Content-Type", "application/json")
                }
            ) {
                assertEquals(HttpStatusCode.InternalServerError, response.status())
            }
        }
    }

    @Test
    fun `Test Move group Mixed`() {
        authorizedTest { user, token, transaction ->
            val version = transaction.createVersion(user)
            val parentGroup = transaction.createGroup(version)
            val group1 = transaction.createGroup(version, parentGroup.vid)
            val group2 = transaction.createGroup(version, parentGroup.vid)
            testMoveGroupPositive(version, group1, group2, token)
        }
        authorizedTest { user, token, transaction ->
            val version = transaction.createVersion(user)
            val parentGroup = transaction.createGroup(version)
            val group1 = transaction.createGroup(version, parentGroup.vid)
            val group2 = transaction.createGroup(version, parentGroup.vid)
            testMoveGroupPositive(version, group2, group1, token)
        }
        authorizedTest { user, token, transaction ->
            val version = transaction.createVersion(user)
            val parentGroup1 = transaction.createGroup(version)
            val parentGroup2 = transaction.createGroup(version)
            val group1 = transaction.createGroup(version, parentGroup1.vid)
            val group2 = transaction.createGroup(version, parentGroup2.vid)
            testMoveGroupNegative(version, group1, group2, token)
        }
    }

    private fun TestApplicationEngine.testMoveGroupPositive(version: Version, group1: Group, group2: Group, token: String) {
        val dto = ChangeGroupPositionDto(group2.id.value)
        handleRequest(HttpMethod.Patch, "/version/${version.id}/group/${group1.id}/position") {
            addHeader("Authorization", "Bearer $token")
            addHeader("Content-Type", "application/json")
            setBody(Json.encodeToString(dto))
        }.apply {
            assertEquals(HttpStatusCode.NoContent, response.status())
            assertEquals(GroupRepo.findById(group1.id.value).order, group2.order)
            assertEquals(GroupRepo.findById(group2.id.value).order, group1.order)
        }
    }

    private fun TestApplicationEngine.testMoveGroupNegative(version: Version, group1: Group, group2: Group, token: String) {
        val dto = ChangeGroupPositionDto(group2.id.value)
        handleRequest(HttpMethod.Patch, "/version/${version.id}/group/${group1.id}/position") {
            addHeader("Authorization", "Bearer $token")
            addHeader("Content-Type", "application/json")
            setBody(Json.encodeToString(dto))
        }.apply {
            assertEquals(HttpStatusCode.BadRequest, response.status())
        }
    }
}
