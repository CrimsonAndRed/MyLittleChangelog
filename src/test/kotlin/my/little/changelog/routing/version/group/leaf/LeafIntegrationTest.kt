package my.little.changelog.routing.version.group.leaf

import io.ktor.http.*
import io.ktor.server.testing.*
import io.ktor.util.*
import kotlinx.serialization.decodeFromString
import my.little.changelog.configuration.Json
import my.little.changelog.model.group.Group
import my.little.changelog.model.leaf.Leaf
import my.little.changelog.model.leaf.LeafType
import my.little.changelog.model.leaf.dto.external.ChangeLeafPositionDto
import my.little.changelog.model.leaf.dto.external.LeafCreationDto
import my.little.changelog.model.leaf.dto.external.LeafReturnedDto
import my.little.changelog.model.leaf.dto.external.LeafUpdateDto
import my.little.changelog.model.version.Version
import my.little.changelog.persistence.repo.LeafRepo
import my.little.changelog.routing.AbstractIntegrationTest
import org.jetbrains.exposed.dao.exceptions.EntityNotFoundException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

internal class LeafIntegrationTest : AbstractIntegrationTest() {

    @Test
    fun `Test Create Leaf Success`() {
        authorizedTest { user, token, transaction ->
            val version = transaction.createVersion(user)
            val group = transaction.createGroup(version)

            val dto = LeafCreationDto(
                null,
                "Test Name 1",
                LeafType.TEXTUAL.id,
                "Test Value 1"
            )

            testAuthorizedRequest(
                HttpMethod.Post,
                "version/${version.id.value}/group/${group.id.value}/leaf",
                token,
                dto
            ) {

                assertEquals(HttpStatusCode.OK, response.status())
                val response = Json.decodeFromString<LeafReturnedDto>(response.content!!)
                assertEquals(dto.name, response.name)
                assertEquals(dto.value, response.value)
                assertEquals(dto.valueType, response.valueType)
            }
        }
    }

    @Test
    fun `Test Create Leaf With Old Version Failure`() {
        authorizedTest { user, token, transaction ->
            val version = transaction.createVersion(user)
            transaction.createVersion(user)
            val group = transaction.createGroup(version)
            val dto = LeafCreationDto(
                null,
                "Test Name 1",
                LeafType.TEXTUAL.id,
                "Test Value 1"
            )

            testAuthorizedRequest(
                HttpMethod.Post,
                "version/${version.id.value}/group/${group.id.value}/leaf",
                token,
                dto
            ) {
                assertEquals(HttpStatusCode.BadRequest, response.status())
                val response = Json.decodeFromString<List<String>>(response.content!!)
                assertTrue { 1 >= response.size }
            }
        }
    }

    @Test
    fun `Test Create Leaf With Nonexistent Version Failure`() {
        authorizedTest { user, token, transaction ->
            val version = transaction.createVersion(user)
            val group = transaction.createGroup(version)
            val dto = LeafCreationDto(
                null,
                "Test Name 1",
                LeafType.TEXTUAL.id,
                "Test Value 1"
            )

            testAuthorizedRequest(
                HttpMethod.Post,
                "version/${version.id.value + 1}/group/${group.id.value}/leaf",
                token,
                dto
            ) {
                assertEquals(HttpStatusCode.InternalServerError, response.status())
            }
        }
    }

    @Test
    fun `Test Create Leaf With Nonexistent Group Failure`() {
        authorizedTest { user, token, transaction ->
            val version = transaction.createVersion(user)
            val group = transaction.createGroup(version)
            val dto = LeafCreationDto(
                null,
                "Test Name 1",
                LeafType.TEXTUAL.id,
                "Test Value 1"
            )

            testAuthorizedRequest(
                HttpMethod.Post,
                "version/${version.id.value + 1}/group/${group.id.value + 1}/leaf",
                token,
                dto
            ) {
                assertEquals(HttpStatusCode.InternalServerError, response.status())
            }
        }
    }

    @Test
    fun `Test Update Leaf Without Group Vid Success`() {
        authorizedTest { user, token, transaction ->
            val version = transaction.createVersion(user)
            val group = transaction.createGroup(version)
            val leaf = transaction.createLeaf(version, group.vid)
            val dto = LeafUpdateDto(
                "Test Name 1",
                LeafType.TEXTUAL.id,
                "Test Value 2",
                group.vid
            )

            testAuthorizedRequest(
                HttpMethod.Put,
                "version/${version.id.value}/group/${group.id.value}/leaf/${leaf.id.value}",
                token,
                dto
            ) {
                assertEquals(HttpStatusCode.NoContent, response.status())
            }
        }
    }

    @Test
    fun `Test Update Leaf With Group Vid Success`() {
        authorizedTest { user, token, transaction ->
            val version = transaction.createVersion(user)
            val group = transaction.createGroup(version)
            val leaf = transaction.createLeaf(version, group.vid)
            val dto = LeafUpdateDto(
                "Test Name 1",
                LeafType.TEXTUAL.id,
                "Value1",
                group.vid
            )

            testAuthorizedRequest(
                HttpMethod.Put,
                "version/${version.id.value}/group/${group.id.value}/leaf/${leaf.id.value}",
                token,
                dto
            ) {
                assertEquals(HttpStatusCode.NoContent, response.status())
            }
        }
    }

    @Test
    fun `Test Update Leaf With Changing Group Success`() {
        authorizedTest { user, token, transaction ->
            val version = transaction.createVersion(user)
            val group1 = transaction.createGroup(version)
            val group2 = transaction.createGroup(version)
            val leaf = transaction.createLeaf(version, group1.vid)

            val dto = LeafUpdateDto(
                name = leaf.name,
                valueType = leaf.valueType,
                value = leaf.value,
                parentVid = group2.vid,
            )

            testAuthorizedRequest(
                HttpMethod.Put,
                "version/${version.id.value}/group/${group1.id.value}/leaf/${leaf.id.value}",
                token,
                dto
            ) {
                assertEquals(HttpStatusCode.NoContent, response.status())
            }
        }
    }

    @Test
    fun `Test Update Leaf With Nonexistent Version Success`() {
        authorizedTest { user, token, transaction ->
            val version = transaction.createVersion(user)
            val group = transaction.createGroup(version)
            val leaf = transaction.createLeaf(version, group.vid)

            val dto = LeafUpdateDto(
                "Leaf1",
                1,
                "Value1",
                group.vid
            )

            testAuthorizedRequest(
                HttpMethod.Put,
                "version/${version.id.value + 1}/group/${group.id.value}/leaf/${leaf.id.value}",
                token,
                dto
            ) {
                assertEquals(HttpStatusCode.NoContent, response.status())
            }
        }
    }

    @Test
    fun `Test Update Leaf With Nonexistent Group Failure`() {
        authorizedTest { user, token, transaction ->
            val version = transaction.createVersion(user)
            val group = transaction.createGroup(version)
            val leaf = transaction.createLeaf(version, group.vid)
            val dto = LeafUpdateDto(
                "Leaf1",
                LeafType.TEXTUAL.id,
                "Value1",
                group.vid + 1
            )

            testAuthorizedRequest(
                HttpMethod.Put,
                "version/${version.id.value}/group/${group.id.value}/leaf/${leaf.id.value}",
                token,
                dto
            ) {
                assertEquals(HttpStatusCode.InternalServerError, response.status())
            }
        }
    }

    @Test
    fun `Test Delete Leaf Success`() {
        authorizedTest { user, token, transaction ->
            val version = transaction.createVersion(user)
            val group = transaction.createGroup(version)
            val leaf = transaction.createLeaf(version, group.vid)

            testAuthorizedRequest(
                HttpMethod.Delete,
                "version/${version.id.value}/group/${group.id.value}/leaf/${leaf.id.value}",
                token,
            ) {
                assertEquals(HttpStatusCode.NoContent, response.status())
            }
            assertThrows<EntityNotFoundException> { Leaf[leaf.id] }
        }
    }

    @Test
    fun `Test Delete Leaf with Wrong Version Failure`() {
        authorizedTest { user, token, transaction ->
            val version1 = transaction.createVersion(user)
            val version2 = transaction.createVersion(user)
            val group = transaction.createGroup(version1)
            val leaf = transaction.createLeaf(version1, group.vid)

            testAuthorizedRequest(
                HttpMethod.Delete,
                "version/${version2.id.value}/group/${group.id.value}/leaf/${leaf.id.value}",
                token
            ) {
                assertEquals(HttpStatusCode.BadRequest, response.status())
            }
            assertNotNull(Leaf[leaf.id])
        }
    }

    @Test
    fun `Test Delete Nonexistent Leaf Failure`() {
        authorizedTest { user, token, transaction ->
            val version1 = transaction.createVersion(user)
            val group = transaction.createGroup(version1)
            val leaf = transaction.createLeaf(version1, group.vid)

            testAuthorizedRequest(
                HttpMethod.Delete,
                "version/${version1.id.value}/group/${group.id.value}/leaf/${leaf.id.value + 1}",
                token
            ) {
                assertEquals(HttpStatusCode.InternalServerError, response.status())
            }
            assertNotNull(Leaf[leaf.id])
        }
    }

    @Test
    fun `Test Complete Delete Leaf Success`() {
        authorizedTest { user, token, transaction ->
            val version1 = transaction.createVersion(user)
            val group = transaction.createGroup(version1)
            val leaf = transaction.createLeaf(version1, group.vid)

            testAuthorizedRequest(
                HttpMethod.Delete,
                "version/${version1.id.value}/group/${group.id.value}/leaf/${leaf.id.value}?completely=true",
                token
            ) {
                assertEquals(HttpStatusCode.NoContent, response.status())
                val dbLeaf = LeafRepo.findById(leaf.id.value)
                assertEquals(true, dbLeaf.isDeleted)
            }
            assertNotNull(Leaf[leaf.id])
        }
    }

    @Test
    fun `Test Move leaf Mixed`() {
        authorizedTest { user, token, transaction ->
            val version = transaction.createVersion(user)
            val group = transaction.createGroup(version)
            val leaf1 = transaction.createLeaf(version, group.vid)
            val leaf2 = transaction.createLeaf(version, group.vid)
            testMoveLeafPositive(version, group, leaf1, leaf2, token)
        }
        authorizedTest { user, token, transaction ->
            val version = transaction.createVersion(user)
            val group = transaction.createGroup(version)
            val leaf1 = transaction.createLeaf(version, group.vid)
            val leaf2 = transaction.createLeaf(version, group.vid)
            testMoveLeafPositive(version, group, leaf2, leaf1, token)
        }
        authorizedTest { user, token, transaction ->
            val version = transaction.createVersion(user)
            val group = transaction.createGroup(version)
            val group2 = transaction.createGroup(version)
            val leaf1 = transaction.createLeaf(version, group.vid)
            val leaf2 = transaction.createLeaf(version, group2.vid)
            testMoveLeafNegative(version, group, leaf2, leaf1, token)
        }
    }

    private fun TestApplicationEngine.testMoveLeafPositive(
        version: Version,
        group: Group,
        leaf1: Leaf,
        leaf2: Leaf,
        token: String
    ) {
        val dto = ChangeLeafPositionDto(leaf2.id.value)
        testAuthorizedRequest(HttpMethod.Patch, "/version/${version.id}/group/${group.id}/leaf/${leaf1.id}/position", token, dto) {
            assertEquals(HttpStatusCode.NoContent, response.status())
            assertEquals(LeafRepo.findById(leaf1.id.value).order, leaf2.order)
            assertEquals(LeafRepo.findById(leaf2.id.value).order, leaf1.order)
        }
    }

    private fun TestApplicationEngine.testMoveLeafNegative(
        version: Version,
        group: Group,
        leaf1: Leaf,
        leaf2: Leaf,
        token: String
    ) {
        val dto = ChangeLeafPositionDto(leaf2.id.value)
        testAuthorizedRequest(HttpMethod.Patch, "/version/${version.id}/group/${group.id}/leaf/${leaf1.id}/position", token, dto) {
            assertEquals(HttpStatusCode.BadRequest, response.status())
        }
    }
}
