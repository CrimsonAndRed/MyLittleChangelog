package my.little.changelog.routing.version

import io.ktor.http.*
import io.ktor.util.*
import kotlinx.serialization.decodeFromString
import my.little.changelog.configuration.Json
import my.little.changelog.model.group.dto.external.WholeGroupDto
import my.little.changelog.model.leaf.dto.external.WholeLeafDto
import my.little.changelog.model.version.dto.external.PreviousVersionsDTO
import my.little.changelog.model.version.dto.external.ReturnedVersionDto
import my.little.changelog.model.version.dto.external.WholeVersion
import my.little.changelog.persistence.repo.GroupRepo
import my.little.changelog.persistence.repo.LeafRepo
import my.little.changelog.persistence.repo.VersionRepo
import my.little.changelog.routing.AbstractIntegrationTest
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test

internal class VersionIntegrationTest : AbstractIntegrationTest() {

    @Test
    fun `Test Get Simple Version Success`() {
        authorizedTest { user, token, transaction ->
            val project = transaction.createProject(user)
            val version = transaction.createVersion(user, project)

            testAuthorizedRequest(HttpMethod.Get, "version/${version.id.value}", token) {
                assertEquals(HttpStatusCode.OK, response.status())
            }
        }
    }

    @Test
    fun `Test Get Version Success`() {
        authorizedTest { user, token, transaction ->

            val project = transaction.createProject(user)
            val version = transaction.createVersion(user, project)
            val group1 = transaction.createGroup(version)
            val group2 = transaction.createGroup(version, null, group1.vid)
            val leaf = transaction.createLeaf(version, group1.vid)

            val group2Dto = WholeGroupDto(
                id = group2.id.value,
                vid = group2.vid,
                name = group2.name,
                realNode = true,
                groupContent = emptyList(),
                leafContent = emptyList(),
                isEarliest = true,
                isDeleted = false
            )

            val leafDto = WholeLeafDto(
                id = leaf.id.value,
                vid = leaf.vid,
                name = leaf.name,
                valueType = leaf.valueType,
                value = leaf.value,
                isDeleted = false
            )

            val group1Dto = WholeGroupDto(
                id = group1.id.value,
                vid = group1.vid,
                name = group1.name,
                realNode = true,
                groupContent = listOf(group2Dto),
                leafContent = listOf(leafDto),
                isEarliest = true,
                isDeleted = false
            )

            val wholeVersion = WholeVersion(
                id = version.id.value,
                canChange = true,
                groupContent = listOf(group1Dto),
                name = version.name,
                projectId = project.id.value
            )

            testAuthorizedRequest(HttpMethod.Get, "version/${version.id}", token) {
                assertEquals(HttpStatusCode.OK, response.status())
                val json: WholeVersion = Json.decodeFromString(response.content!!)
                assertEquals(json, wholeVersion)
            }
        }
    }

    @Test
    fun `Test Get Versions Success`() {
        authorizedTest { user, token, transaction ->
            val project = transaction.createProject(user)
            transaction.createVersion(user, project)
            transaction.createVersion(user, project)

            testAuthorizedRequest(HttpMethod.Get, "project/${project.id}/version", token) {
                assertEquals(HttpStatusCode.OK, response.status())
                val jsonList: List<ReturnedVersionDto> = Json.decodeFromString(response.content!!)
                assertEquals(2, jsonList.size)
            }
        }
    }

    @Test
    fun `Test Delete Version Success`() {
        authorizedTest { user, token, transaction ->
            val project = transaction.createProject(user)
            val firstVersion = transaction.createVersion(user, project)
            val latestVersion = transaction.createVersion(user, project)
            val g1 = transaction.createGroup(latestVersion)
            transaction.createGroup(latestVersion, null, g1.vid)
            transaction.createLeaf(latestVersion, g1.vid)

            testAuthorizedRequest(HttpMethod.Delete, "version/${latestVersion.id.value}", token) {
                transaction {
                    assertEquals(HttpStatusCode.NoContent, response.status())
                    assertEquals(firstVersion.id, VersionRepo.findLatestByProject(project).id)
                    assertEquals(0, GroupRepo.findAll().count())
                    assertEquals(0, LeafRepo.findAll().count())
                }
            }
        }
    }

    @Test
    fun `Test Delete Other User Version Failure`() {
        authorizedTest { user, token, transaction ->
            val otherUser = transaction.createUser()
            val project = transaction.createProject(otherUser)
            val version = transaction.createVersion(user, project)

            testAuthorizedRequest(HttpMethod.Delete, "version/${version.id.value}", token) {
                transaction {
                    assertEquals(HttpStatusCode.Forbidden, response.status())
                }
            }
        }
    }


    @Test
    fun `Test Delete Not Latest Version Failure`() {
        authorizedTest { user, token, transaction ->
            val project = transaction.createProject(user)
            val firstVersion = transaction.createVersion(user, project)
            val latestVersion = transaction.createVersion(user, project)
            val group = transaction.createGroup(latestVersion)
            transaction.createLeaf(latestVersion, group.vid)

            testAuthorizedRequest(HttpMethod.Delete, "version/${firstVersion.id.value}", token) {
                transaction {
                    assertEquals(HttpStatusCode.BadRequest, response.status())
                    assertNotEquals(firstVersion, VersionRepo.findLatestByProject(project))
                    assertEquals(1, GroupRepo.findAll().count())
                    assertEquals(1, LeafRepo.findAll().count())
                }
            }
        }
    }

    @Test
    fun `Test Delete Nonexistent Version Failure`() {
        authorizedTest { user, token, transaction ->
            val project = transaction.createProject(user)
            val firstVersion = transaction.createVersion(user, project)
            transaction.createGroup(firstVersion)

            testAuthorizedRequest(HttpMethod.Delete, "version/${firstVersion.id.value + 1}", token) {
                transaction {
                    assertEquals(HttpStatusCode.InternalServerError, response.status())
                }
            }
        }
    }

    @Test
    fun `Test Delete Latest Version With Other Project Success`() {
        authorizedTest { user, token, transaction ->
            val project = transaction.createProject(user)
            val firstVersion = transaction.createVersion(user, project)
            val project2 = transaction.createProject(user)
            val version2 = transaction.createVersion(user, project2)
            transaction.createGroup(firstVersion)

            testAuthorizedRequest(HttpMethod.Delete, "version/${firstVersion.id.value}", token) {
                transaction {
                    assertEquals(HttpStatusCode.NoContent, response.status())
                }
            }
        }
    }

    @Test
    fun `Test Get Previous Version Success`() {
        authorizedTest { user, token, transaction ->
            val project = transaction.createProject(user)
            val latestVersion = transaction.createVersion(user, project)
            val group = transaction.createGroup(latestVersion)
            val leaf = transaction.createLeaf(latestVersion, group.vid)
            testAuthorizedRequest(HttpMethod.Get, "project/${project.id}/version/previous", token) {
                transaction {
                    assertEquals(HttpStatusCode.OK, response.status())
                    val json: PreviousVersionsDTO = Json.decodeFromString(response.content!!)
                    assertEquals(1, json.groupContent.size)
                    assertEquals(1, json.groupContent[0].leafContent.size)

                    val groupContent = json.groupContent[0]

                    assertEquals(groupContent.id, group.id.value)
                    assertEquals(groupContent.vid, group.vid)
                    assertEquals(groupContent.name, group.name)
                    assertEquals(groupContent.groupContent.size, 0)
                    assertEquals(groupContent.leafContent.size, 1)

                    val leafContent = json.groupContent[0].leafContent[0]

                    assertEquals(leafContent.id, leaf.id.value)
                    assertEquals(leafContent.vid, leaf.vid)
                    assertEquals(leafContent.name, leaf.name)
                    assertEquals(leafContent.valueType, leaf.valueType)
                    assertEquals(leafContent.value, leaf.value)
                }
            }
        }
    }

    @Test
    fun `Test Get Versions Preserve Order Success`() {
        authorizedTest { user, token, transaction ->
            val project = transaction.createProject(user)
            val v1 = transaction.createVersion(user, project)
            val v2 = transaction.createVersion(user, project)

            testAuthorizedRequest(HttpMethod.Get, "project/${project.id}/version", token) {
                assertEquals(HttpStatusCode.OK, response.status())
                val jsonList: List<ReturnedVersionDto> = Json.decodeFromString(response.content!!)
                assertEquals(2, jsonList.size)
                assertEquals(v1.id.value, jsonList[0].id)
                assertEquals(v2.id.value, jsonList[1].id)
            }
        }
    }
}
