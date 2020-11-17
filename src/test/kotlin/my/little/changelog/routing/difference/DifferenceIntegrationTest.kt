package my.little.changelog.routing.difference

import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.handleRequest
import io.ktor.util.KtorExperimentalAPI
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import my.little.changelog.model.diff.dto.external.ReturnedDifferenceDto
import my.little.changelog.model.leaf.Leaf
import my.little.changelog.model.version.Version
import my.little.changelog.routing.AbstractIntegrationTest
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

@KtorExperimentalAPI
internal class DifferenceIntegrationTest : AbstractIntegrationTest() {

    @Test
    fun `Test Get Difference Empty Success`() {
        testApplication {
            val version1 = transaction {
                Version.new {}
            }
            val version2 = transaction {
                Version.new {}
            }

            with(handleRequest(HttpMethod.Get, "difference?from=${version1.id.value}&to=${version2.id.value}")) {
                assertEquals(HttpStatusCode.OK, response.status())
                val responseBody = Json.decodeFromString<ReturnedDifferenceDto>(response.content!!)
                assertEquals(version1.id.value, responseBody.from)
                assertEquals(version2.id.value, responseBody.to)
                assertEquals(0, responseBody.groupContent.size)
                assertEquals(0, responseBody.leafContent.size)
            }
        }
    }

    @Test
    fun `Test Get Difference One Leaf Success`() {
        testApplication {
            val version1 = transaction {
                Version.new {}
            }
            val version2 = transaction {
                Version.new {}
            }

            val leaf = transaction {
                Leaf.new {
                    valueType = 1
                    name = "Test"
                    value = "1"
                    version = version2
                    groupVid = null
                }
            }

            with(handleRequest(HttpMethod.Get, "difference?from=${version1.id.value}&to=${version2.id.value}")) {
                assertEquals(HttpStatusCode.OK, response.status())
                val responseBody = Json.decodeFromString<ReturnedDifferenceDto>(response.content!!)
                assertEquals(version1.id.value, responseBody.from)
                assertEquals(version2.id.value, responseBody.to)
                assertEquals(0, responseBody.groupContent.size)
                assertEquals(1, responseBody.leafContent.size)
                val leafDiff = responseBody.leafContent[0]
                assertEquals(leaf.id.value, leafDiff.id)
                assertEquals(leaf.vid, leafDiff.vid)
                assertEquals(leaf.name, leafDiff.name)
                assertEquals(leaf.valueType, leafDiff.valueType)
                assertEquals(" -> ${leaf.value}", leafDiff.valueDiff)
            }
        }
    }

    @Test
    fun `Test Get Difference With Nonexistent Version Failure`() {
        testApplication {
            val version1 = transaction {
                Version.new {}
            }
            val version2 = transaction {
                Version.new {}
            }

            with(handleRequest(HttpMethod.Get, "difference?from=${version1.id.value}&to=${version2.id.value + 1}")) {
                assertEquals(HttpStatusCode.InternalServerError, response.status())
            }
        }
    }

    @Test
    fun `Test Get Difference With Param Missing Failure`() {
        testApplication {
            val version1 = transaction {
                Version.new {}
            }
            transaction {
                Version.new {}
            }

            with(handleRequest(HttpMethod.Get, "difference?from=${version1.id.value}")) {
                assertEquals(HttpStatusCode.InternalServerError, response.status())
            }
        }
    }
}
