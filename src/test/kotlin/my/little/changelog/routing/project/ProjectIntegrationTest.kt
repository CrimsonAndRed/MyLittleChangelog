package my.little.changelog.routing.project

import io.ktor.http.*
import kotlinx.serialization.decodeFromString
import my.little.changelog.configuration.Json
import my.little.changelog.model.project.dto.external.ProjectCreationDto
import my.little.changelog.model.project.dto.external.ReturnedProjectDto
import my.little.changelog.model.version.dto.external.ReturnedVersionDto
import my.little.changelog.routing.AbstractIntegrationTest
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class ProjectIntegrationTest : AbstractIntegrationTest() {

    @Test
    fun `Test Create Project Success`() {
        authorizedTest { _, token, _ ->
            val dto = ProjectCreationDto("test", "test")
            testAuthorizedRequest(HttpMethod.Post, "project", token, dto) {
                assertEquals(HttpStatusCode.OK, response.status())
            }
        }
    }

    @Test
    fun `Test Get Project Versions Success`() {
        authorizedTest { user, token, transaction ->
            val project = transaction.createProject(user)
            transaction.createVersion(user, project)

            testAuthorizedRequest(HttpMethod.Get, "project/${project.id}/version", token) {
                assertEquals(HttpStatusCode.OK, response.status())
                val jsonList: List<ReturnedVersionDto> = Json.decodeFromString(response.content!!)
                assertEquals(1, jsonList.size)
            }
        }
    }

    @Test
    fun `Test Get Nonexistent Project Versions Failure`() {
        authorizedTest { user, token, transaction ->
            val project = transaction.createProject(user)
            transaction.createVersion(user, project)

            testAuthorizedRequest(HttpMethod.Get, "project/${project.id.value + 1}/version", token) {
                assertEquals(HttpStatusCode.InternalServerError, response.status())
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
    fun `Test Delete Project With Versions Success`() {
        authorizedTest { user, token, transaction ->
            val project = transaction.createProject(user)
            val firstVersion = transaction.createVersion(user, project)
            val latestVersion = transaction.createVersion(user, project)
            val group = transaction.createGroup(latestVersion)
            transaction.createLeaf(latestVersion, group.vid)

            testAuthorizedRequest(HttpMethod.Delete, "project/${project.id.value}", token) {
                transaction {
                    assertEquals(HttpStatusCode.NoContent, response.status())
                }
            }
        }
    }

    @Test
    fun `Test Delete Nonexistent Project Failure`() {
        authorizedTest { user, token, transaction ->
            val project = transaction.createProject(user)

            testAuthorizedRequest(HttpMethod.Delete, "project/${project.id.value + 1}", token) {
                transaction {
                    assertEquals(HttpStatusCode.InternalServerError, response.status())
                }
            }
        }
    }

    @Test
    fun `Test Get Projects Preserve Order Success`() {
        authorizedTest { user, token, transaction ->
            val project1 = transaction.createProject(user)
            val project2 = transaction.createProject(user)

            testAuthorizedRequest(HttpMethod.Get, "project", token) {
                assertEquals(HttpStatusCode.OK, response.status())
                val jsonList: List<ReturnedProjectDto> = Json.decodeFromString(response.content!!)
                assertEquals(2, jsonList.size)
                assertEquals(project1.id.value, jsonList[0].id)
                assertEquals(project2.id.value, jsonList[1].id)
            }
        }
    }
}
