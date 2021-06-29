package my.little.changelog.routing.project

import io.ktor.http.*
import io.mockk.every
import io.mockk.mockkObject
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import my.little.changelog.exception.ForbiddenException
import my.little.changelog.exception.UnauthException
import my.little.changelog.model.project.dto.external.ProjectCreationDto
import my.little.changelog.model.project.dto.service.ReturnedProjectDto
import my.little.changelog.routing.AbstractRouterTest
import my.little.changelog.service.project.ProjectService
import my.little.changelog.validator.Valid
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class ProjectRouterTest : AbstractRouterTest(
    { projectRouting() }
) {

    init {
        mockkObject(ProjectService)
    }

    private val baseUrl = "project"

    @Test
    fun `Test Projects Read Success`() {

        val dtos = listOf(ReturnedProjectDto(0, "test", "test", 0))

        every { ProjectService.getProjects(any()) } returns dtos

        testAuthorizedRoute(HttpMethod.Get, "project") {
            assertEquals(HttpStatusCode.OK, response.status())
            val resp = Json.decodeFromString<List<my.little.changelog.model.project.dto.external.ReturnedProjectDto>>(response.content!!)
            assertEquals(dtos.size, resp.size)
        }
    }

    @Test
    fun `Test Projects Read Exception`() {
        testAuthorizedExceptions(
            constructAuthorizedRequest(HttpMethod.Get, "project"),
            listOf { ProjectService.getProjects(any()) },
            listOf(
                { RuntimeException() } to HttpStatusCode.InternalServerError,
                { UnauthException() } to HttpStatusCode.Unauthorized,
                { ForbiddenException() } to HttpStatusCode.Forbidden,
            )
        )
    }

    @Test
    fun `Test Project Create Success`() {
        val dto = ReturnedProjectDto(0, "test", "test", 0)
        val createDto = ProjectCreationDto("test", "test")

        every { ProjectService.createProject(any()) } returns dto

        testAuthorizedRoute(HttpMethod.Post, baseUrl, createDto) {
            assertEquals(HttpStatusCode.OK, response.status())
        }
    }

    @Test
    fun `Test Project Create Exception`() {
        val createDto = ProjectCreationDto("test", "test")
        testAuthorizedExceptions(
            constructAuthorizedRequest(HttpMethod.Post, baseUrl, createDto),
            listOf { ProjectService.createProject(any()) },
            listOf(
                { RuntimeException() } to HttpStatusCode.InternalServerError,
                { UnauthException() } to HttpStatusCode.Unauthorized,
                { ForbiddenException() } to HttpStatusCode.Forbidden,
            )
        )
    }

    @Test
    fun `Test Project Delete Success`() {
        every { ProjectService.deleteProject(any()) } returns Valid(Unit)

        testAuthorizedRoute(HttpMethod.Delete, "$baseUrl/0") {
            assertEquals(HttpStatusCode.NoContent, response.status())
        }
    }

    @Test
    fun `Test Project Delete Exception`() {

        testAuthorizedExceptions(
            constructAuthorizedRequest(HttpMethod.Delete, "$baseUrl/0"),
            listOf { ProjectService.deleteProject(any()) },
            listOf(
                { RuntimeException() } to HttpStatusCode.InternalServerError,
                { UnauthException() } to HttpStatusCode.Unauthorized,
                { ForbiddenException() } to HttpStatusCode.Forbidden,
            )
        )
    }
}
