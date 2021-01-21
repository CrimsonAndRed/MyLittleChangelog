package my.little.changelog.routing.difference

import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.util.KtorExperimentalAPI
import io.mockk.every
import io.mockk.mockkObject
import my.little.changelog.model.diff.dto.service.ReturnedDifferenceDto
import my.little.changelog.routing.AbstractRouterTest
import my.little.changelog.routing.diff.differenceRouting
import my.little.changelog.service.diff.DifferenceService
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

@KtorExperimentalAPI
internal class DifferenceRouterTest : AbstractRouterTest(
    { differenceRouting() }
) {
    init {
        mockkObject(DifferenceService)
    }

    private val baseUrl: (Int, Int) -> String = { from, to ->
        "difference?from=$from&to=$to"
    }
    private val difference = Difference(0, 1)

    @Test
    fun `Test Difference Read Success`() {
        val returnedDto = ReturnedDifferenceDto(0, 1, emptyList(), emptyList())

        every { DifferenceService.findDifference(allAny()) } returns returnedDto

        testRoute(HttpMethod.Get, baseUrl(difference.from, difference.to)) {
            assertEquals(HttpStatusCode.OK, response.status())
        }
    }

    @Test
    fun `Test Difference Read Exception`() = testExceptions(
        constructRequest(HttpMethod.Get, baseUrl(difference.from, difference.to)),
        listOf { DifferenceService.findDifference(allAny()) },
        listOf(
            { RuntimeException() } to HttpStatusCode.InternalServerError,
        )
    )

    @Test
    fun `Test Difference Missing From`() {
        val returnedDto = ReturnedDifferenceDto(0, 1, emptyList(), emptyList())

        every { DifferenceService.findDifference(allAny()) } returns returnedDto
        testRoute(HttpMethod.Get, "difference?to=${difference.to}") {
            assertEquals(HttpStatusCode.InternalServerError, response.status())
        }
    }

    @Test
    fun `Test Difference Missing To`() {
        val returnedDto = ReturnedDifferenceDto(0, 1, emptyList(), emptyList())

        every { DifferenceService.findDifference(allAny()) } returns returnedDto
        testRoute(HttpMethod.Get, "difference?from=${difference.from}") {
            assertEquals(HttpStatusCode.InternalServerError, response.status())
        }
    }

    @Test
    fun `Test Difference Missing Params`() {
        val returnedDto = ReturnedDifferenceDto(0, 1, emptyList(), emptyList())

        every { DifferenceService.findDifference(allAny()) } returns returnedDto
        testRoute(HttpMethod.Get, "difference") {
            assertEquals(HttpStatusCode.InternalServerError, response.status())
        }
    }

    @Test
    fun `Test Difference Wrong From Param Type`() {
        val returnedDto = ReturnedDifferenceDto(0, 1, emptyList(), emptyList())

        every { DifferenceService.findDifference(allAny()) } returns returnedDto
        testRoute(HttpMethod.Get, "difference?from=aa&to=${difference.to}") {
            assertEquals(HttpStatusCode.InternalServerError, response.status())
        }
    }

    @Test
    fun `Test Difference Wrong To Param Type`() {
        val returnedDto = ReturnedDifferenceDto(0, 1, emptyList(), emptyList())

        every { DifferenceService.findDifference(allAny()) } returns returnedDto
        testRoute(HttpMethod.Get, "difference?to=aa&from=${difference.from}") {
            assertEquals(HttpStatusCode.InternalServerError, response.status())
        }
    }

    data class Difference(val from: Int, val to: Int)
}
