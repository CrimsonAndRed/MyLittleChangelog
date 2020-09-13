package my.little.changelog.routing.version.group.leaf

import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.util.KtorExperimentalAPI
import io.mockk.every
import io.mockk.mockkObject
import kotlinx.serialization.decodeFromString
import my.little.changelog.configuration.Json
import my.little.changelog.model.leaf.dto.external.LeafCreationDto
import my.little.changelog.model.leaf.dto.external.LeafReturnedDto
import my.little.changelog.model.leaf.dto.external.LeafUpdateDto
import my.little.changelog.model.leaf.dto.service.toExternalDto
import my.little.changelog.routing.AbstractRouterTest
import my.little.changelog.service.LeafService
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

@KtorExperimentalAPI
internal class LeafRouterKtTest : AbstractRouterTest({
    leafRouting()
}) {

    init {
        mockkObject(LeafService)
    }

    private val baseVgl = VersionGroupLeaf(0, 0, 0)
    private val baseUrl: (Int, Int) -> String = { v, g -> "version/$v/group/$g/leaf" }

    @Test
    fun `Test Leaf Creation`() {
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
    fun `Test Leaf Update`() {
        val dto = LeafUpdateDto("Имя1", 1, "Значение1", 0)
        val serviceReturnedDto = my.little.changelog.model.leaf.dto.service.LeafReturnedDto(baseVgl.l, baseVgl.l, dto.name, dto.valueType, dto.value)

        every { LeafService.updateLeaf(allAny()) } returns serviceReturnedDto
        testRoute(HttpMethod.Put, "${baseUrl(baseVgl.v, baseVgl.g)}/${baseVgl.l}", dto) {
            assertEquals(HttpStatusCode.OK, response.status())
            val response = Json.decodeFromString<LeafReturnedDto>(response.content!!)
            assertEquals(serviceReturnedDto.toExternalDto(), response)
        }
    }

    data class VersionGroupLeaf(val v: Int, val g: Int, val l: Int)
}
