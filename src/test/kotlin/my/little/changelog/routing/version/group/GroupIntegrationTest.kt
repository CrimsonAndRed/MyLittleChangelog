package my.little.changelog.routing.version.group

import io.ktor.http.*
import io.ktor.server.testing.*
import io.ktor.util.*
import kotlinx.serialization.decodeFromString
import my.little.changelog.configuration.Json
import my.little.changelog.model.group.Group
import my.little.changelog.model.group.dto.external.ChangeGroupPositionDto
import my.little.changelog.model.group.dto.external.GroupCreationDto
import my.little.changelog.model.group.dto.external.GroupUpdateDto
import my.little.changelog.model.group.dto.external.ReturnedGroupDto
import my.little.changelog.model.leaf.Leaf
import my.little.changelog.model.version.Version
import my.little.changelog.persistence.repo.GroupLatestRepo
import my.little.changelog.persistence.repo.GroupRepo
import my.little.changelog.routing.AbstractIntegrationTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.test.assertNotEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

internal class GroupIntegrationTest : AbstractIntegrationTest() {

    @Test
    fun `Test Root Group Creation Success`() {
        authorizedTest { user, token, transaction ->
            val project = transaction.createProject(user)
            val version = transaction.createVersion(user, project)
            val dto = GroupCreationDto("Test Name 1")

            testAuthorizedRequest(HttpMethod.Post, "version/${version.id.value}/group", token, dto) {
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
            val project = transaction.createProject(user)
            val version = transaction.createVersion(user, project)
            val group = transaction.createGroup(version)
            val dto = GroupCreationDto("Test Name 1", parentVid = group.vid)

            testAuthorizedRequest(HttpMethod.Post, "version/${version.id.value}/group", token, dto) {
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
            val project = transaction.createProject(user)
            val version1 = transaction.createVersion(user, project)
            val version2 = transaction.createVersion(user, project)
            val group = transaction.createGroup(version1)
            val dto = GroupCreationDto(group.name, vid = group.vid)

            testAuthorizedRequest(HttpMethod.Post, "version/${version2.id.value}/group", token, dto) {
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
            val project = transaction.createProject(user)
            val version1 = transaction.createVersion(user, project)
            val version2 = transaction.createVersion(user, project)
            val group = transaction.createGroup(version1)
            val dto = GroupCreationDto(group.name, vid = group.vid)

            testAuthorizedRequest(HttpMethod.Post, "version/${version2.id.value + 1}/group", token, dto) {
                assertEquals(HttpStatusCode.InternalServerError, response.status())
            }
        }
    }

    @Test
    fun `Test Group Create With Nonexistent Parent Group Failure`() {
        authorizedTest { user, token, transaction ->
            val project = transaction.createProject(user)
            val version1 = transaction.createVersion(user, project)
            val version2 = transaction.createVersion(user, project)
            val group = transaction.createGroup(version1)
            val dto = GroupCreationDto(group.name, vid = group.vid + 1)

            testAuthorizedRequest(HttpMethod.Post, "version/$version2/group", token, dto) {
                assertEquals(HttpStatusCode.InternalServerError, response.status())
            }
        }
    }

    @Test
    fun `Test Group Update Name Success`() {
        authorizedTest { user, token, transaction ->
            val project = transaction.createProject(user)
            val version = transaction.createVersion(user, project)
            val group = transaction.createGroup(version)
            val dto = GroupUpdateDto("Test Base Group 2", null)

            testAuthorizedRequest(HttpMethod.Put, "version/${version.id.value}/group/${group.id.value}", token, dto) {
                assertEquals(HttpStatusCode.NoContent, response.status())
            }
        }
    }

    @Test
    fun `Test Group Update Parent Success`() {
        authorizedTest { user, token, transaction ->
            val project = transaction.createProject(user)
            val version = transaction.createVersion(user, project)
            val groupRoot = transaction.createGroup(version)
            val groupSub = transaction.createGroup(version)

            val dto = GroupUpdateDto("Test Base Group 2", groupRoot.id.value)

            testAuthorizedRequest(HttpMethod.Put, "version/${version.id.value}/group/${groupSub.id.value}", token, dto) {
                assertEquals(HttpStatusCode.NoContent, response.status())
            }
        }
    }

    @Test
    fun `Test Group Update With Old Version Failure`() {
        authorizedTest { user, token, transaction ->
            val project = transaction.createProject(user)
            val version = transaction.createVersion(user, project)
            transaction.createVersion(user, project)
            val group = transaction.createGroup(version)
            val dto = GroupUpdateDto("Test Base Group 2", null)

            testAuthorizedRequest(HttpMethod.Put, "version/${version.id.value}/group/${group.id.value}", token, dto) {
                assertEquals(HttpStatusCode.BadRequest, response.status())
                val response = Json.decodeFromString<List<String>>(response.content!!)
                assertTrue { 1 >= response.size }
            }
        }
    }

    @Test
    fun `Test Group Delete Success`() {
        authorizedTest { user, token, transaction ->
            val project = transaction.createProject(user)
            val version = transaction.createVersion(user, project)
            val group = transaction.createGroup(version)
            testAuthorizedRequest(
                HttpMethod.Delete,
                "version/${version.id.value}/group/${group.id.value}?hierarchy=true",
                token
            ) {
                assertEquals(HttpStatusCode.NoContent, response.status())
            }
        }
    }

    @Test
    fun `Test Group Delete With Old Version Failure`() {
        authorizedTest { user, token, transaction ->
            val project = transaction.createProject(user)
            val version = transaction.createVersion(user, project)
            val version2 = transaction.createVersion(user, project)
            val group = transaction.createGroup(version)

            testAuthorizedRequest(
                HttpMethod.Delete,
                "version/${version2.id.value}/group/${group.id.value}?hierarchy=true",
                token
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
            val project = transaction.createProject(user)
            val version = transaction.createVersion(user, project)
            val group = transaction.createGroup(version)
            transaction.createLeaf(version, group.vid)
            testAuthorizedRequest(
                HttpMethod.Delete,
                "version/${version.id.value}/group/${group.id.value}?hierarchy=false",
                token
            ) {
                assertEquals(HttpStatusCode.BadRequest, response.status())
            }
        }
    }

    @Test
    fun `Test Group Delete Hierarchy Success`() {
        authorizedTest { user, token, transaction ->
            val project = transaction.createProject(user)
            val version = transaction.createVersion(user, project)
            val group = transaction.createGroup(version)
            val leaf = transaction.createLeaf(version, group.vid)
            testAuthorizedRequest(
                HttpMethod.Delete,
                "version/${version.id.value}/group/${group.id.value}?hierarchy=true",
                token
            ) {
                assertEquals(HttpStatusCode.NoContent, response.status())
            }

            assertNull(Leaf.findById(leaf.id.value))
        }
    }

    @Test
    fun `Test Group Delete With Nonexistent Version Success`() {
        authorizedTest { user, token, transaction ->
            val project = transaction.createProject(user)
            val version = transaction.createVersion(user, project)
            val group = transaction.createGroup(version)
            testAuthorizedRequest(
                HttpMethod.Delete,
                "version/${version.id.value + 1}/group/${group.id.value}?hierarchy=true",
                token
            ) {
                assertEquals(HttpStatusCode.NoContent, response.status())
            }
        }
    }

    @Test
    fun `Test Group Delete With Nonexistent Group Failure`() {
        authorizedTest { user, token, transaction ->
            val project = transaction.createProject(user)
            val version = transaction.createVersion(user, project)
            val group = transaction.createGroup(version)
            testAuthorizedRequest(
                HttpMethod.Delete,
                "version/${version.id.value}/group/${group.id.value + 1}?hierarchy=true",
                token
            ) {
                assertEquals(HttpStatusCode.InternalServerError, response.status())
            }
        }
    }

    @Test
    fun `Test Group Complete Delete Success`() {
        authorizedTest { user, token, transaction ->
            val project = transaction.createProject(user)
            val version = transaction.createVersion(user, project)
            val group1 = transaction.createGroup(version)
            val group2 = transaction.createGroup(version, null, group1.vid)
            testAuthorizedRequest(
                HttpMethod.Delete,
                "version/${version.id.value}/group/${group1.id.value}?completely=true",
                token
            ) {
                assertEquals(HttpStatusCode.NoContent, response.status())
                val group2After = GroupRepo.findById(group2.id.value)
                assertEquals(true, group2After.isDeleted)
            }
        }
    }

    @Test
    fun `Test Group Complete Delete Fetch From Prev Success`() {
        authorizedTest { user, token, transaction ->
            val project = transaction.createProject(user)
            val version1 = transaction.createVersion(user, project)
            val group1 = transaction.createGroup(version1)
            val group2 = transaction.createGroup(version1, null, group1.vid, "Test", false)
            val version2 = transaction.createVersion(user, project)
            val group3 = transaction.createGroup(version2, group1.vid)
            testAuthorizedRequest(
                HttpMethod.Delete,
                "version/${version2.id.value}/group/${group3.id.value}?completely=true",
                token
            ) {
                assertEquals(HttpStatusCode.NoContent, response.status())

                val group2After = GroupLatestRepo.findByVid(group2.vid)
                assertEquals(true, group2After.isDeleted)
                assertEquals(version2.id.value, group2After.version.id.value)
            }
        }
    }

    @Test
    fun `Test Group Complete Delete Already Deleted Child Success`() {
        authorizedTest { user, token, transaction ->
            val project = transaction.createProject(user)
            val version1 = transaction.createVersion(user, project)
            val group1 = transaction.createGroup(version1)
            val group2 = transaction.createGroup(version1, null, group1.vid, "Test", true)
            val version2 = transaction.createVersion(user, project)
            val group3 = transaction.createGroup(version2, group1.vid, group1.parentVid)
            testAuthorizedRequest(
                HttpMethod.Delete,
                "version/${version2.id.value}/group/${group3.id.value}?completely=true",
                token
            ) {
                assertEquals(HttpStatusCode.NoContent, response.status())

                val group2After = GroupLatestRepo.findByVid(group2.vid)
                assertEquals(true, group2After.isDeleted)
                assertEquals(version1.id.value, group2After.version.id.value)
            }
        }
    }

    @Test
    fun `Test Move group Mixed`() {
        authorizedTest { user, token, transaction ->
            val project = transaction.createProject(user)
            val version = transaction.createVersion(user, project)
            val parentGroup = transaction.createGroup(version)
            val group1 = transaction.createGroup(version, null, parentGroup.vid)
            val group2 = transaction.createGroup(version, null, parentGroup.vid)
            testMoveGroupPositive(version, group1, group2, token)
        }
        authorizedTest { user, token, transaction ->
            val project = transaction.createProject(user)
            val version = transaction.createVersion(user, project)
            val parentGroup = transaction.createGroup(version)
            val group1 = transaction.createGroup(version, null, parentGroup.vid)
            val group2 = transaction.createGroup(version, null, parentGroup.vid)
            testMoveGroupPositive(version, group2, group1, token)
        }
        authorizedTest { user, token, transaction ->
            val project = transaction.createProject(user)
            val version = transaction.createVersion(user, project)
            val parentGroup1 = transaction.createGroup(version)
            val parentGroup2 = transaction.createGroup(version)
            val group1 = transaction.createGroup(version, null, parentGroup1.vid)
            val group2 = transaction.createGroup(version, null, parentGroup2.vid)
            testMoveGroupNegative(version, group1, group2, token)
        }
    }

    private fun TestApplicationEngine.testMoveGroupPositive(
        version: Version,
        group1: Group,
        group2: Group,
        token: String
    ) {
        val dto = ChangeGroupPositionDto(group2.id.value)
        testAuthorizedRequest(
            HttpMethod.Patch,
            "/version/${version.id}/group/${group1.id}/position",
            token,
            dto
        ) {
            assertEquals(HttpStatusCode.NoContent, response.status())
            assertEquals(GroupRepo.findById(group1.id.value).order, group2.order)
            assertEquals(GroupRepo.findById(group2.id.value).order, group1.order)
        }
    }

    private fun TestApplicationEngine.testMoveGroupNegative(
        version: Version,
        group1: Group,
        group2: Group,
        token: String
    ) {
        val dto = ChangeGroupPositionDto(group2.id.value)
        testAuthorizedRequest(HttpMethod.Patch, "/version/${version.id}/group/${group1.id}/position", token, dto) {
            assertEquals(HttpStatusCode.BadRequest, response.status())
        }
    }
}
