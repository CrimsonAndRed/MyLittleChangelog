package my.little.changelog.routing.version

import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.util.KtorExperimentalAPI
import io.mockk.every
import io.mockk.mockkObject
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import my.little.changelog.model.exception.VersionIsNotLatestException
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
        val dto = ReturnedVersionDto(0)

        every { VersionService.createVersion() } returns dto

        testRoute(HttpMethod.Post, baseUrl) {
            assertEquals(HttpStatusCode.OK, response.status())
        }
    }

    @Test
    fun `Test Version Create Exception`() = testExceptions(
        constructRequest(HttpMethod.Post, baseUrl),
        listOf { VersionService.createVersion() },
        listOf(
            { RuntimeException() } to HttpStatusCode.InternalServerError,
            { VersionIsNotLatestException() } to HttpStatusCode.InternalServerError
        )
    )

    @Test
    fun `Test Version Read Success`() {
        val dto = WholeVersion(0, emptyList(), emptyList())

        every { VersionService.getWholeVersion(any()) } returns dto

        testRoute(HttpMethod.Get, "$baseUrl/${dto.id}") {
            assertEquals(HttpStatusCode.OK, response.status())
            val resp = Json.decodeFromString<WholeVersion>(response.content!!)
            assertEquals(dto.id, resp.id)
            assertEquals(dto.groupContent, resp.groupContent)
            assertEquals(dto.leafContent, resp.leafContent)
        }
    }

    @Test
    fun `Test Version Read Exception`() = testExceptions(
        constructRequest(HttpMethod.Get, "$baseUrl/$0"),
        listOf { VersionService.getWholeVersion(any()) },
        listOf(
            { RuntimeException() } to HttpStatusCode.InternalServerError,
            { VersionIsNotLatestException() } to HttpStatusCode.InternalServerError
        )
    )
}
