package my.little.changelog.routing.version.group

import io.ktor.http.*
import io.ktor.util.*
import kotlinx.serialization.decodeFromString
import my.little.changelog.configuration.Json
import my.little.changelog.model.group.dto.external.ChangeGroupPositionDto
import my.little.changelog.model.group.dto.external.GroupCreationDto
import my.little.changelog.model.group.dto.external.GroupUpdateDto
import my.little.changelog.routing.AbstractIntegrationTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

internal class GroupIntegrationValidationTest : AbstractIntegrationTest() {
    @Test
    fun `Test Group Create With Blank Name Failure`() {
        authorizedTest { user, token, transaction ->
            val project = transaction.createProject(user)
            val version1 = transaction.createVersion(user, project)
            val dto = GroupCreationDto(" ")

            testAuthorizedRequest(HttpMethod.Post, "version/${version1.id.value}/group", token, dto) {
                Assertions.assertEquals(HttpStatusCode.BadRequest, response.status())
                val response = Json.decodeFromString<List<String>>(response.content!!)
                assertTrue { 1 >= response.size }
            }
        }
    }

    @Test
    fun `Test Group Update With Blank Name Failure`() {
        authorizedTest { user, token, transaction ->
            val project = transaction.createProject(user)
            val version1 = transaction.createVersion(user, project)
            val group = transaction.createGroup(version1)
            val dto = GroupUpdateDto(" ")

            testAuthorizedRequest(HttpMethod.Put, "version/${version1.id.value}/group/${group.id.value}", token, dto) {
                Assertions.assertEquals(HttpStatusCode.BadRequest, response.status())
                val response = Json.decodeFromString<List<String>>(response.content!!)
                assertTrue { 1 >= response.size }
            }
        }
    }

    @Test
    fun `Test Group Move To Child Failure`() {
        authorizedTest { user, token, transaction ->
            val project = transaction.createProject(user)
            val version1 = transaction.createVersion(user, project)
            val group = transaction.createGroup(version1)
            val group2 = transaction.createGroup(version1, null, group.vid)
            val dto = ChangeGroupPositionDto(group2.vid)

            testAuthorizedRequest(HttpMethod.Patch, "version/${version1.id.value}/group/${group.id.value}/position", token, dto) {
                Assertions.assertEquals(HttpStatusCode.BadRequest, response.status())
                val response = Json.decodeFromString<List<String>>(response.content!!)
                assertTrue { 1 >= response.size }
            }
        }
    }
}
