package my.little.changelog.routing.version.group.leaf

import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.util.KtorExperimentalAPI
import io.mockk.every
import io.mockk.mockkObject
import kotlinx.serialization.decodeFromString
import my.little.changelog.configuration.Json
import my.little.changelog.model.exception.VersionIsNotLatestException
import my.little.changelog.model.leaf.dto.external.LeafCreationDto
import my.little.changelog.model.leaf.dto.external.LeafReturnedDto
import my.little.changelog.model.leaf.dto.external.LeafUpdateDto
import my.little.changelog.model.leaf.dto.service.toExternalDto
import my.little.changelog.routing.AbstractRouterTest
import my.little.changelog.service.leaf.LeafService
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

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
        val dto = LeafCreationDto("Имя1", 1, "Значение1")
        val serviceReturnedDto = my.little.changelog.model.leaf.dto.service.LeafReturnedDto(baseVgl.l, baseVgl.l, dto.name, dto.valueType, dto.value)

        every { LeafService.createLeaf(allAny()) } returns serviceReturnedDto

        testRoute(HttpMethod.Post, baseUrl(baseVgl.v, baseVgl.g), dto) {
            assertEquals(HttpStatusCode.OK, response.status())
            val response = Json.decodeFromString<LeafReturnedDto>(response.content!!)
            assertEquals(serviceReturnedDto.toExternalDto(), response)
        }
    }

    @Test
    fun `Test Leaf Creation Exception`() {
        val dto = LeafCreationDto("Имя1", 1, "Значение1")

        testExceptions(
            constructRequest(HttpMethod.Post, baseUrl(baseVgl.v, baseVgl.g), dto),
            listOf { LeafService.createLeaf(allAny()) },
            listOf(
                { RuntimeException() } to HttpStatusCode.InternalServerError,
                { VersionIsNotLatestException() } to HttpStatusCode.InternalServerError
            )
        )
    }

    @Test
    fun `Test Leaf Update Success`() {
        val dto = LeafUpdateDto("Имя1", 1, "Значение1", 0)
        val serviceReturnedDto = my.little.changelog.model.leaf.dto.service.LeafReturnedDto(baseVgl.l, baseVgl.l, dto.name, dto.valueType, dto.value)

        every { LeafService.updateLeaf(allAny()) } returns serviceReturnedDto
        testRoute(HttpMethod.Put, "${baseUrl(baseVgl.v, baseVgl.g)}/${baseVgl.l}", dto) {
            assertEquals(HttpStatusCode.OK, response.status())
            val response = Json.decodeFromString<LeafReturnedDto>(response.content!!)
            assertEquals(serviceReturnedDto.toExternalDto(), response)
        }
    }

    @Test
    fun `Test Leaf Update Exception`() {
        val dto = LeafUpdateDto("Имя1", 1, "Значение1", 0)

        testExceptions(
            constructRequest(HttpMethod.Put, "${baseUrl(baseVgl.v, baseVgl.g)}/${baseVgl.l}", dto),
            listOf { LeafService.updateLeaf(allAny()) },
            listOf(
                { RuntimeException() } to HttpStatusCode.InternalServerError,
                { VersionIsNotLatestException() } to HttpStatusCode.InternalServerError
            )
        )
    }

    @Test
    fun `Test Leaf Delete Success`() {
        every { LeafService.deleteLeaf(allAny()) } returns Unit
        testRoute(HttpMethod.Delete, "${baseUrl(baseVgl.v, baseVgl.g)}/${baseVgl.l}") {
            assertEquals(HttpStatusCode.NoContent, response.status())
            assertNull(response.content)
        }
    }

    @Test
    fun `Test Leaf Delete Exceptions`() = testExceptions(
        constructRequest(HttpMethod.Delete, "${baseUrl(baseVgl.v, baseVgl.g)}/${baseVgl.l}"),
        listOf { LeafService.deleteLeaf(allAny()) },
        listOf(
            { RuntimeException() } to HttpStatusCode.InternalServerError,
            { VersionIsNotLatestException() } to HttpStatusCode.InternalServerError
        )
    )

    data class VersionGroupLeaf(val v: Int, val g: Int, val l: Int)
}
