package my.little.changelog.routing.version.group

import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.util.KtorExperimentalAPI
import io.mockk.every
import io.mockk.mockkObject
import kotlinx.serialization.decodeFromString
import my.little.changelog.configuration.Json
import my.little.changelog.model.exception.VersionIsNotLatestException
import my.little.changelog.model.group.dto.external.GroupCreationDto
import my.little.changelog.model.group.dto.external.GroupUpdateDto
import my.little.changelog.model.group.dto.external.ReturnedGroupDto
import my.little.changelog.routing.AbstractRouterTest
import my.little.changelog.service.group.GroupService
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

@KtorExperimentalAPI
internal class GroupRouterTest : AbstractRouterTest(
    { groupRouting() }
) {

    private val vg = VersionGroup(0, 0)
    private val baseUrl: (Int) -> String = { v -> "version/$v/group" }

    init {
        mockkObject(GroupService)
    }

    @Test
    fun `Test Create Group Success`() {
        val dto = GroupCreationDto("Test")
        val serviceDto = my.little.changelog.model.group.dto.service.ReturnedGroupDto(0, 0, "Test")

        every { GroupService.createGroup(any()) } returns serviceDto

        testRoute(HttpMethod.Post, baseUrl(vg.v), dto) {
            assertEquals(HttpStatusCode.OK, response.status())
            val resp = Json.decodeFromString<ReturnedGroupDto>(response.content!!)

            assertEquals(serviceDto.id, resp.id)
            assertEquals(serviceDto.vid, resp.vid)
            assertEquals(serviceDto.name, resp.name)
            assertEquals(serviceDto.parent?.id, resp.parentId)
        }
    }

    @Test
    fun `Test Create Group Exception`() {
        val dto = ReturnedGroupDto(0, 0, "Test")
        testExceptions(
            constructRequest(HttpMethod.Post, baseUrl(vg.v), dto),
            listOf { GroupService.createGroup(any()) },
            listOf(
                { RuntimeException() } to HttpStatusCode.InternalServerError,
                { VersionIsNotLatestException() } to HttpStatusCode.InternalServerError
            )
        )
    }

    @Test
    fun `Test Update Group Success`() {
        val dto = GroupUpdateDto("Test")
        val serviceDto = my.little.changelog.model.group.dto.service.ReturnedGroupDto(0, 0, "Test")

        every { GroupService.updateGroup(any()) } returns serviceDto

        testRoute(HttpMethod.Put, "${baseUrl(vg.v)}/${vg.g}", dto) {
            assertEquals(HttpStatusCode.OK, response.status())
            val resp = Json.decodeFromString<ReturnedGroupDto>(response.content!!)

            assertEquals(serviceDto.id, resp.id)
            assertEquals(serviceDto.vid, resp.vid)
            assertEquals(serviceDto.name, resp.name)
            assertEquals(serviceDto.parent?.id, resp.parentId)
        }
    }

    @Test
    fun `Test Update Group Exception`() {
        val dto = ReturnedGroupDto(0, 0, "Test")
        testExceptions(
            constructRequest(HttpMethod.Put, "${baseUrl(vg.v)}/${vg.g}", dto),
            listOf { GroupService.updateGroup(any()) },
            listOf(
                { RuntimeException() } to HttpStatusCode.InternalServerError,
                { VersionIsNotLatestException() } to HttpStatusCode.InternalServerError
            )
        )
    }

    data class VersionGroup(val v: Int, val g: Int)
}
