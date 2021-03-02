package my.little.changelog.routing.version

import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.util.KtorExperimentalAPI
import io.mockk.every
import io.mockk.mockkObject
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
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
        val dto = ReturnedVersionDto(0, "test")
        val createDto = VersionCreationDto("test")

        every { VersionService.createVersion(any()) } returns dto

        testRoute(HttpMethod.Post, baseUrl, createDto) {
            assertEquals(HttpStatusCode.OK, response.status())
        }
    }

    @Test
    fun `Test Version Create Exception`() {
        val createDto = VersionCreationDto("test")
        testExceptions(
            constructRequest(HttpMethod.Post, baseUrl, createDto),
            listOf { VersionService.createVersion(any()) },
            listOf(
                { RuntimeException() } to HttpStatusCode.InternalServerError
            )
        )
    }

    @Test
    fun `Test Version Read Success`() {
        val dto = WholeVersion(0, true, emptyList())

        every { VersionService.getWholeVersion(any()) } returns dto

        testRoute(HttpMethod.Get, "$baseUrl/${dto.id}") {
            assertEquals(HttpStatusCode.OK, response.status())
            val resp = Json.decodeFromString<WholeVersion>(response.content!!)
            assertEquals(dto.id, resp.id)
            assertEquals(dto.groupContent, resp.groupContent)
        }
    }

    @Test
    fun `Test Version Read Exception`() = testExceptions(
        constructRequest(HttpMethod.Get, "$baseUrl/$0"),
        listOf { VersionService.getWholeVersion(any()) },
        listOf(
            { RuntimeException() } to HttpStatusCode.InternalServerError
        )
    )

    @Test
    fun `Test Versions Get Success`() {
        val dto = ReturnedVersionDto(0, "test")

        every { VersionService.getVersions() } returns listOf(dto)

        testRoute(HttpMethod.Get, baseUrl) {
            assertEquals(HttpStatusCode.OK, response.status())
        }
    }

    @Test
    fun `Test Versions Get Exception`() = testExceptions(
        constructRequest(HttpMethod.Get, baseUrl),
        listOf { VersionService.getVersions() },
        listOf(
            { RuntimeException() } to HttpStatusCode.InternalServerError
        )
    )

    @Test
    fun `Test Previous Versions Get Success`() {
        val dto = PreviousVersionsDTO(emptyList())

        every { VersionService.getPreviousVersions() } returns dto

        testRoute(HttpMethod.Get, "$baseUrl/previous") {
            assertEquals(HttpStatusCode.OK, response.status())
        }
    }

    @Test
    fun `Test Previous Versions Get Exception`() = testExceptions(
        constructRequest(HttpMethod.Get, "$baseUrl/previous"),
        listOf { VersionService.getPreviousVersions() },
        listOf(
            { RuntimeException() } to HttpStatusCode.InternalServerError,
        )
    )
}
