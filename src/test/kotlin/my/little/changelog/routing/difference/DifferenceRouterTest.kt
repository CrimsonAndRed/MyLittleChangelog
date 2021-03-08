package my.little.changelog.routing.difference

import io.ktor.http.*
import io.ktor.util.*
import io.mockk.every
import io.mockk.mockkObject
import my.little.changelog.exception.ForbiddenException
import my.little.changelog.exception.UnauthException
import my.little.changelog.model.diff.dto.service.ReturnedDifferenceDto
import my.little.changelog.model.version.dto.service.ReturnedVersionDto
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
        val v1 = ReturnedVersionDto(1, "test1", 1)
        val v2 = ReturnedVersionDto(2, "test2", 2)
        val returnedDto = ReturnedDifferenceDto(v1, v2, emptyList(), emptyList())

        every { DifferenceService.findDifference(allAny(), allAny()) } returns returnedDto

        testAuthorizedRoute(HttpMethod.Get, baseUrl(difference.from, difference.to)) {
            assertEquals(HttpStatusCode.OK, response.status())
        }
    }

    @Test
    fun `Test Difference Read Exception`() = testAuthorizedExceptions(
        constructAuthorizedRequest(HttpMethod.Get, baseUrl(difference.from, difference.to)),
        listOf { DifferenceService.findDifference(allAny(), allAny()) },
        listOf(
            { RuntimeException() } to HttpStatusCode.InternalServerError,
            { UnauthException() } to HttpStatusCode.Unauthorized,
            { ForbiddenException() } to HttpStatusCode.Forbidden,
        )
    )

    @Test
    fun `Test Difference Missing From Failure`() {
        val v1 = ReturnedVersionDto(1, "test1", 1)
        val v2 = ReturnedVersionDto(2, "test2", 2)
        val returnedDto = ReturnedDifferenceDto(v1, v2, emptyList(), emptyList())

        every { DifferenceService.findDifference(allAny(), allAny()) } returns returnedDto
        testAuthorizedRoute(HttpMethod.Get, "difference?to=${difference.to}") {
            assertEquals(HttpStatusCode.InternalServerError, response.status())
        }
    }

    @Test
    fun `Test Difference Missing To Failure`() {
        val v1 = ReturnedVersionDto(1, "test1", 1)
        val v2 = ReturnedVersionDto(2, "test2", 2)
        val returnedDto = ReturnedDifferenceDto(v1, v2, emptyList(), emptyList())

        every { DifferenceService.findDifference(allAny(), allAny()) } returns returnedDto
        testAuthorizedRoute(HttpMethod.Get, "difference?from=${difference.from}") {
            assertEquals(HttpStatusCode.InternalServerError, response.status())
        }
    }

    @Test
    fun `Test Difference Missing Params Failure`() {
        val v1 = ReturnedVersionDto(1, "test1", 1)
        val v2 = ReturnedVersionDto(2, "test2", 2)
        val returnedDto = ReturnedDifferenceDto(v1, v2, emptyList(), emptyList())

        every { DifferenceService.findDifference(allAny(), allAny()) } returns returnedDto
        testAuthorizedRoute(HttpMethod.Get, "difference") {
            assertEquals(HttpStatusCode.InternalServerError, response.status())
        }
    }

    @Test
    fun `Test Difference Wrong From Param Type Failure`() {
        val v1 = ReturnedVersionDto(1, "test1", 1)
        val v2 = ReturnedVersionDto(2, "test2", 2)
        val returnedDto = ReturnedDifferenceDto(v1, v2, emptyList(), emptyList())

        every { DifferenceService.findDifference(allAny(), allAny()) } returns returnedDto
        testAuthorizedRoute(HttpMethod.Get, "difference?from=aa&to=${difference.to}") {
            assertEquals(HttpStatusCode.InternalServerError, response.status())
        }
    }

    @Test
    fun `Test Difference Wrong To Param Type Failure`() {
        val v1 = ReturnedVersionDto(1, "test1", 1)
        val v2 = ReturnedVersionDto(2, "test2", 2)
        val returnedDto = ReturnedDifferenceDto(v1, v2, emptyList(), emptyList())

        every { DifferenceService.findDifference(allAny(), allAny()) } returns returnedDto
        testAuthorizedRoute(HttpMethod.Get, "difference?to=aa&from=${difference.from}") {
            assertEquals(HttpStatusCode.InternalServerError, response.status())
        }
    }

    data class Difference(val from: Int, val to: Int)
}
