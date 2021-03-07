package my.little.changelog.routing.version

import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.util.KtorExperimentalAPI
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
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

@KtorExperimentalAPI
internal class VersionRouterTest : AbstractRouterTest(
    { versionRouting() }
) {

    init {
        mockkObject(VersionService)
    }

    private val baseUrl = "version"

    @Test
    fun `Test Version Create Success`() {
        val dto = ReturnedVersionDto(0, "test", 0)
        val createDto = VersionCreationDto("test")

        every { VersionService.createVersion(any(), any()) } returns dto

        testAuthorizedRoute(HttpMethod.Post, baseUrl, createDto) {
            assertEquals(HttpStatusCode.OK, response.status())
        }
    }

    @Test
    fun `Test Version Create Exception`() {
        val createDto = VersionCreationDto("test")
        testAuthorizedExceptions(
            constructAuthorizedRequest(HttpMethod.Post, baseUrl, createDto),
            listOf { VersionService.createVersion(any(), any()) },
            listOf(
                { RuntimeException() } to HttpStatusCode.InternalServerError,
                { UnauthException() } to HttpStatusCode.Unauthorized,
                { ForbiddenException() } to HttpStatusCode.Forbidden,
            )
        )
    }

    @Test
    fun `Test Version Read Success`() {
        val dto = WholeVersion(0, true, emptyList(), "test")

        every { VersionService.getWholeVersion(any(), allAny()) } returns dto

        testAuthorizedRoute(HttpMethod.Get, "$baseUrl/${dto.id}") {
            assertEquals(HttpStatusCode.OK, response.status())
            val resp = Json.decodeFromString<WholeVersion>(response.content!!)
            assertEquals(dto.id, resp.id)
            assertEquals(dto.groupContent, resp.groupContent)
        }
    }

    @Test
    fun `Test Version Read Exception`() {
        val dto = WholeVersion(0, true, emptyList(), "test")
        testAuthorizedExceptions(
            constructAuthorizedRequest(HttpMethod.Get, "$baseUrl/${dto.id}"),
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

        every { VersionService.getVersions(allAny()) } returns listOf(dto)

        testAuthorizedRoute(HttpMethod.Get, baseUrl) {
            assertEquals(HttpStatusCode.OK, response.status())
        }
    }

    @Test
    fun `Test Versions Get Exception`() = testAuthorizedExceptions(
        constructAuthorizedRequest(HttpMethod.Get, baseUrl),
        listOf { VersionService.getVersions(allAny()) },
        listOf(
            { RuntimeException() } to HttpStatusCode.InternalServerError,
            { UnauthException() } to HttpStatusCode.Unauthorized,
            { ForbiddenException() } to HttpStatusCode.Forbidden,
        )
    )

    @Test
    fun `Test Previous Versions Get Success`() {
        val dto = PreviousVersionsDTO(emptyList())

        every { VersionService.getPreviousVersions(allAny()) } returns dto

        testAuthorizedRoute(HttpMethod.Get, "$baseUrl/previous") {
            assertEquals(HttpStatusCode.OK, response.status())
        }
    }

    @Test
    fun `Test Previous Versions Get Exception`() = testAuthorizedExceptions(
        constructAuthorizedRequest(HttpMethod.Get, "$baseUrl/previous"),
        listOf { VersionService.getPreviousVersions(allAny()) },
        listOf(
            { RuntimeException() } to HttpStatusCode.InternalServerError,
            { UnauthException() } to HttpStatusCode.Unauthorized,
            { ForbiddenException() } to HttpStatusCode.Forbidden,
        )
    )
}
