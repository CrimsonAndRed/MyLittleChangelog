package my.little.changelog.routing.difference

import io.ktor.http.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import my.little.changelog.model.diff.dto.external.ReturnedDifferenceDto
import my.little.changelog.routing.AbstractIntegrationTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class DifferenceIntegrationTest : AbstractIntegrationTest() {

    @Test
    fun `Test Get Difference Empty Success`() {
        authorizedTest { user, token, transaction ->
            val project = transaction.createProject(user)
            val version1 = transaction.createVersion(user, project)
            val version2 = transaction.createVersion(user, project)

            testAuthorizedRequest(
                HttpMethod.Get,
                "difference?from=${version1.id.value}&to=${version2.id.value}",
                token
            ) {
                assertEquals(HttpStatusCode.OK, response.status())
                val responseBody = Json.decodeFromString<ReturnedDifferenceDto>(response.content!!)
                assertEquals(version1.id.value, responseBody.from.id)
                assertEquals(version2.id.value, responseBody.to.id)
                assertEquals(0, responseBody.groupContent.size)
                assertEquals(0, responseBody.leafContent.size)
            }
        }
    }

    @Test
    fun `Test Get Difference One Leaf Success`() {
        authorizedTest { user, token, transaction ->
            val project = transaction.createProject(user)
            val version1 = transaction.createVersion(user, project)
            val version2 = transaction.createVersion(user, project)
            val group = transaction.createGroup(version2)
            val leaf = transaction.createLeaf(version2, group.vid)

            testAuthorizedRequest(
                HttpMethod.Get,
                "difference?from=${version1.id.value}&to=${version2.id.value}",
                token
            ) {
                assertEquals(HttpStatusCode.OK, response.status())
                val responseBody = Json.decodeFromString<ReturnedDifferenceDto>(response.content!!)
                assertEquals(version1.id.value, responseBody.from.id)
                assertEquals(version2.id.value, responseBody.to.id)
                assertEquals(1, responseBody.groupContent.size)
                assertEquals(0, responseBody.leafContent.size)
                val groupDiff = responseBody.groupContent[0]
                assertEquals(0, groupDiff.groupContent.size)
                assertEquals(1, groupDiff.leafContent.size)
                val leafDiff = groupDiff.leafContent[0]
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
        authorizedTest { user, token, transaction ->
            val project = transaction.createProject(user)
            val version1 = transaction.createVersion(user, project)
            val version2 = transaction.createVersion(user, project)

            testAuthorizedRequest(
                HttpMethod.Get,
                "difference?from=${version1.id.value}&to=${version2.id.value + 1}",
                token
            ) {
                assertEquals(HttpStatusCode.InternalServerError, response.status())
            }
        }
    }

    @Test
    fun `Test Get Difference With Param Missing Failure`() {
        authorizedTest { user, token, transaction ->
            val project = transaction.createProject(user)
            val version1 = transaction.createVersion(user, project)
            transaction.createVersion(user, project)
            testAuthorizedRequest(HttpMethod.Get, "difference?from=${version1.id.value}", token) {
                assertEquals(HttpStatusCode.InternalServerError, response.status())
            }
        }
    }
}
