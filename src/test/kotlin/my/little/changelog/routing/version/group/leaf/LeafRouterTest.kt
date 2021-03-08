package my.little.changelog.routing.version.group.leaf

import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.util.KtorExperimentalAPI
import io.mockk.every
import io.mockk.mockkObject
import kotlinx.serialization.decodeFromString
import my.little.changelog.configuration.Json
import my.little.changelog.model.leaf.LeafType
import my.little.changelog.model.leaf.dto.external.ChangeLeafPositionDto
import my.little.changelog.model.leaf.dto.external.LeafCreationDto
import my.little.changelog.model.leaf.dto.external.LeafReturnedDto
import my.little.changelog.model.leaf.dto.external.LeafUpdateDto
import my.little.changelog.model.leaf.dto.service.toExternalDto
import my.little.changelog.routing.AbstractRouterTest
import my.little.changelog.service.leaf.LeafService
import my.little.changelog.validator.Err
import my.little.changelog.validator.Valid
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

@KtorExperimentalAPI
internal class LeafRouterTest : AbstractRouterTest({
    leafRouting()
}) {

    init {
        mockkObject(LeafService)
    }

    private val baseVgl = VersionGroupLeaf(0, 0, 0)
    private val baseUrl: (Int, Int) -> String = { v, g -> "version/$v/group/$g/leaf" }

    @Test
    fun `Test Leaf Creation Success`() {
        val dto = LeafCreationDto(null, "Test Name 1", LeafType.TEXTUAL.id, "Test Value 1")
        val serviceReturnedDto = my.little.changelog.model.leaf.dto.service.LeafReturnedDto(
            id = baseVgl.l,
            vid = baseVgl.l,
            name = dto.name!!,
            valueType = dto.valueType!!,
            value = dto.value!!,
            groupVid = baseVgl.g
        )

        every { LeafService.createLeaf(allAny()) } returns Valid(serviceReturnedDto)

        testAuthorizedRoute(HttpMethod.Post, baseUrl(baseVgl.v, baseVgl.g), dto) {
            assertEquals(HttpStatusCode.OK, response.status())
            val response = Json.decodeFromString<LeafReturnedDto>(response.content!!)
            assertEquals(serviceReturnedDto.toExternalDto(), response)
        }
    }

    @Test
    fun `Test Leaf Creation Exception`() {
        val dto = LeafCreationDto(null, "Test Name 1", 1, "Test Value 1")

        testExceptions(
            constructAuthorizedRequest(HttpMethod.Post, baseUrl(baseVgl.v, baseVgl.g), dto),
            listOf { LeafService.createLeaf(allAny()) },
            listOf(
                { RuntimeException() } to HttpStatusCode.InternalServerError,
            )
        )
    }

    @Test
    fun `Test Leaf Creation Validation Failure`() {
        val dto = LeafCreationDto(null, "Test Name 1", 1, "Test Value 1")
        every { LeafService.createLeaf(allAny()) } returns Err(listOf("Test error"))

        testAuthorizedRoute(HttpMethod.Post, baseUrl(baseVgl.v, baseVgl.g), dto) {
            assertEquals(HttpStatusCode.BadRequest, response.status())
            val response = Json.decodeFromString<List<String>>(response.content!!)
            assertTrue { 1 >= response.size }
        }
    }

    @Test
    fun `Test Leaf Update Success`() {
        val dto = LeafUpdateDto("Test Name 1", 1, "Test Value 1", 0)

        every { LeafService.updateLeaf(allAny()) } returns Valid(Unit)
        testAuthorizedRoute(HttpMethod.Put, "${baseUrl(baseVgl.v, baseVgl.g)}/${baseVgl.l}", dto) {
            assertEquals(HttpStatusCode.NoContent, response.status())
        }
    }

    @Test
    fun `Test Leaf Update Exception`() {
        val dto = LeafUpdateDto("Test Name 1", 1, "Test Value 1", 0)

        testExceptions(
            constructAuthorizedRequest(HttpMethod.Put, "${baseUrl(baseVgl.v, baseVgl.g)}/${baseVgl.l}", dto),
            listOf { LeafService.updateLeaf(allAny()) },
            listOf(
                { RuntimeException() } to HttpStatusCode.InternalServerError,
            )
        )
    }

    @Test
    fun `Test Leaf Update Validation Failure`() {
        val dto = LeafUpdateDto("Test Name 1", 1, "Test Value 1", 0)
        every { LeafService.updateLeaf(allAny()) } returns Err(listOf("Test error"))

        testAuthorizedRoute(HttpMethod.Put, "${baseUrl(baseVgl.v, baseVgl.g)}/${baseVgl.l}", dto) {
            assertEquals(HttpStatusCode.BadRequest, response.status())
            val response = Json.decodeFromString<List<String>>(response.content!!)
            assertTrue { 1 >= response.size }
        }
    }

    @Test
    fun `Test Leaf Delete Success`() {
        every { LeafService.deleteLeaf(allAny()) } returns Valid(Unit)
        testAuthorizedRoute(HttpMethod.Delete, "${baseUrl(baseVgl.v, baseVgl.g)}/${baseVgl.l}") {
            assertEquals(HttpStatusCode.NoContent, response.status())
            assertNull(response.content)
        }
    }

    @Test
    fun `Test Leaf Delete Exceptions`() = testExceptions(
        constructAuthorizedRequest(HttpMethod.Delete, "${baseUrl(baseVgl.v, baseVgl.g)}/${baseVgl.l}"),
        listOf { LeafService.deleteLeaf(allAny()) },
        listOf(
            { RuntimeException() } to HttpStatusCode.InternalServerError,
        )
    )

    @Test
    fun `Test Leaf Delete Validation Failure`() {
        val dto = LeafCreationDto(null, "Test Name 1", LeafType.TEXTUAL.id, "Test Value 1")
        every { LeafService.deleteLeaf(allAny()) } returns Err(listOf("Test error"))

        testAuthorizedRoute(HttpMethod.Delete, "${baseUrl(baseVgl.v, baseVgl.g)}/${baseVgl.l}", dto) {
            assertEquals(HttpStatusCode.BadRequest, response.status())
            val response = Json.decodeFromString<List<String>>(response.content!!)
            assertTrue { 1 >= response.size }
        }
    }

    @Test
    fun `Test Leaf Change Version Exceptions`() = testExceptions(
        constructAuthorizedRequest(HttpMethod.Patch, "${baseUrl(baseVgl.v, baseVgl.g)}/${baseVgl.l}/position", ChangeLeafPositionDto(0)),
        listOf { LeafService.changePosition(allAny(), allAny(), allAny()) },
        listOf(
            { RuntimeException() } to HttpStatusCode.InternalServerError,
        )
    )

    @Test
    fun `Test Leaf Change Version Validation Failure`() {
        val dto = ChangeLeafPositionDto(0)
        every { LeafService.changePosition(allAny(), allAny(), allAny()) } returns Err(listOf("Test error"))

        testAuthorizedRoute(HttpMethod.Patch, "${baseUrl(baseVgl.v, baseVgl.g)}/${baseVgl.l}/position", dto) {
            assertEquals(HttpStatusCode.BadRequest, response.status())
            val response = Json.decodeFromString<List<String>>(response.content!!)
            assertTrue { 1 >= response.size }
        }
    }

    data class VersionGroupLeaf(val v: Int, val g: Int, val l: Int)
}
