package my.little.changelog.routing.version

import io.ktor.http.*
import io.mockk.every
import io.mockk.mockkObject
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import my.little.changelog.exception.ForbiddenException
import my.little.changelog.exception.UnauthException
import my.little.changelog.model.version.dto.external.PreviousVersionsDTO
import my.little.changelog.model.version.dto.external.VersionCreationDto
import my.little.changelog.model.version.dto.external.WholeVersion
import my.little.changelog.model.version.dto.service.ReturnedVersionDto
import my.little.changelog.routing.AbstractRouterTest
import my.little.changelog.service.version.VersionService
import my.little.changelog.validator.Valid
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class VersionRouterTest : AbstractRouterTest(
    { versionRouting() }
) {

    init {
        mockkObject(VersionService)
    }

    data class Project(val v: Int)

    private val baseUrl: (Int) -> String = { v -> "project/$v/version" }

    @Test
    fun `Test Version Create Success`() {
        val dto = ReturnedVersionDto(0, "test", 0)
        val createDto = VersionCreationDto("test")

        every { VersionService.createVersion(any()) } returns Valid(dto)

        testAuthorizedRoute(HttpMethod.Post, baseUrl(dto.id), createDto) {
            assertEquals(HttpStatusCode.OK, response.status())
        }
    }

    @Test
    fun `Test Version Create Exception`() {
        val createDto = VersionCreationDto("test")
        testAuthorizedExceptions(
            constructAuthorizedRequest(HttpMethod.Post, baseUrl(0), createDto),
            listOf { VersionService.createVersion(any()) },
            listOf(
                { RuntimeException() } to HttpStatusCode.InternalServerError,
                { UnauthException() } to HttpStatusCode.Unauthorized,
                { ForbiddenException() } to HttpStatusCode.Forbidden,
            )
        )
    }

    @Test
    fun `Test Version Read Success`() {
        val dto = WholeVersion(0, true, emptyList(), "test", 0)

        every { VersionService.getWholeVersion(any(), allAny()) } returns dto

        testAuthorizedRoute(HttpMethod.Get, "version/${dto.id}") {
            assertEquals(HttpStatusCode.OK, response.status())
            val resp = Json.decodeFromString<WholeVersion>(response.content!!)
            assertEquals(dto.id, resp.id)
            assertEquals(dto.groupContent, resp.groupContent)
        }
    }

    @Test
    fun `Test Version Read Exception`() {
        val dto = WholeVersion(0, true, emptyList(), "test", 0)
        testAuthorizedExceptions(
            constructAuthorizedRequest(HttpMethod.Get, "version/${dto.id}"),
            listOf { VersionService.getWholeVersion(any(), allAny()) },
            listOf(
                { RuntimeException() } to HttpStatusCode.InternalServerError,
                { UnauthException() } to HttpStatusCode.Unauthorized,
                { ForbiddenException() } to HttpStatusCode.Forbidden,
            )
        )
    }

    @Test
    fun `Test Versions Get Success`() {
        val dto = ReturnedVersionDto(0, "test", 0)

        every { VersionService.getVersions(allAny(), allAny()) } returns listOf(dto)

        testAuthorizedRoute(HttpMethod.Get, baseUrl(dto.id)) {
            assertEquals(HttpStatusCode.OK, response.status())
        }
    }

    @Test
    fun `Test Versions Get Exception`() = testAuthorizedExceptions(
        constructAuthorizedRequest(HttpMethod.Get, baseUrl(0)),
        listOf { VersionService.getVersions(allAny(), allAny()) },
        listOf(
            { RuntimeException() } to HttpStatusCode.InternalServerError,
            { UnauthException() } to HttpStatusCode.Unauthorized,
            { ForbiddenException() } to HttpStatusCode.Forbidden,
        )
    )

    @Test
    fun `Test Previous Versions Get Success`() {
        val dto = PreviousVersionsDTO(emptyList())

        every { VersionService.getPreviousVersions(allAny(), allAny()) } returns dto

        testAuthorizedRoute(HttpMethod.Get, "${baseUrl(0)}/previous") {
            assertEquals(HttpStatusCode.OK, response.status())
        }
    }

    @Test
    fun `Test Previous Versions Get Exception`() = testAuthorizedExceptions(
        constructAuthorizedRequest(HttpMethod.Get, "${baseUrl(0)}/previous"),
        listOf { VersionService.getPreviousVersions(allAny(), allAny()) },
        listOf(
            { RuntimeException() } to HttpStatusCode.InternalServerError,
            { UnauthException() } to HttpStatusCode.Unauthorized,
            { ForbiddenException() } to HttpStatusCode.Forbidden,
        )
    )
}
