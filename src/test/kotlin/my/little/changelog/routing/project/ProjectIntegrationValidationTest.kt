package my.little.changelog.routing.project

import io.ktor.http.*
import kotlinx.serialization.decodeFromString
import my.little.changelog.configuration.Json
import my.little.changelog.model.project.dto.external.ProjectCreationDto
import my.little.changelog.routing.AbstractIntegrationTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

internal class ProjectIntegrationValidationTest : AbstractIntegrationTest() {

    @Test
    fun `Test Project Create With Blank Name Failure`() {
        authorizedTest { _, token, _ ->
            val dto = ProjectCreationDto(null, "test")

            testAuthorizedRequest(HttpMethod.Post, "project", token, dto) {
                Assertions.assertEquals(HttpStatusCode.BadRequest, response.status())
                val response = Json.decodeFromString<List<String>>(response.content!!)
                assertTrue { 1 >= response.size }
            }
        }
    }

    @Test
    fun `Test Project Create With Blank Description Failure`() {
        authorizedTest { _, token, _ ->
            val dto = ProjectCreationDto("test", null)

            testAuthorizedRequest(HttpMethod.Post, "project", token, dto) {
                Assertions.assertEquals(HttpStatusCode.BadRequest, response.status())
                val response = Json.decodeFromString<List<String>>(response.content!!)
                assertTrue { 1 >= response.size }
            }
        }
    }
}
