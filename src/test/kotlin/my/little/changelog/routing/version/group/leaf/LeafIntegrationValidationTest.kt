package my.little.changelog.routing.version.group.leaf

import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.setBody
import io.ktor.util.KtorExperimentalAPI
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import my.little.changelog.configuration.Json
import my.little.changelog.model.leaf.LeafType
import my.little.changelog.model.leaf.dto.external.LeafCreationDto
import my.little.changelog.model.leaf.dto.external.LeafUpdateDto
import my.little.changelog.routing.AbstractIntegrationTest
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

@KtorExperimentalAPI
internal class LeafIntegrationValidationTest : AbstractIntegrationTest() {
    @Test
    fun `Test Create Leaf With Blank Name`() {
        authorizedTest { user, token, transaction ->
            val version = transaction.createVersion(user)
            val group = transaction.createGroup(version)
            val dto = LeafCreationDto(
                null,
                " ",
                LeafType.TEXTUAL.id,
                "Test Value 1"
            )

            with(
                handleRequest(HttpMethod.Post, "version/${version.id.value}/group/${group.id.value}/leaf") {
                    addHeader("Authorization", "Bearer $token")
                    addHeader("Content-Type", "application/json")
                    setBody(Json.encodeToString(dto))
                }
            ) {
                Assertions.assertEquals(HttpStatusCode.BadRequest, response.status())
                val response = Json.decodeFromString<List<String>>(response.content!!)
                assertTrue { 1 >= response.size }
            }
        }
    }

    @Test
    fun `Test Update Leaf With Blank Name`() {
        authorizedTest { user, token, transaction ->
            val version = transaction.createVersion(user)
            val group = transaction.createGroup(version)
            val leaf = transaction.createLeaf(version, group.vid)
            val dto = LeafUpdateDto(
                " ",
                LeafType.TEXTUAL.id,
                "Test Value 1",
                group.vid
            )

            with(
                handleRequest(
                    HttpMethod.Put,
                    "version/${version.id.value}/group/${group.id.value}/leaf/${leaf.id.value}"
                ) {
                    addHeader("Authorization", "Bearer $token")
                    addHeader("Content-Type", "application/json")
                    setBody(Json.encodeToString(dto))
                }
            ) {
                Assertions.assertEquals(HttpStatusCode.BadRequest, response.status())
                val response = Json.decodeFromString<List<String>>(response.content!!)
                assertTrue { 1 >= response.size }
            }
        }
    }

    @Test
    fun `Test Update Leaf With Textual Type`() {
        authorizedTest { user, token, transaction ->
            val version = transaction.createVersion(user)
            val group = transaction.createGroup(version)
            val leaf = transaction.createLeaf(version, group.vid)
            val dto = LeafUpdateDto(
                "Test Name 1",
                LeafType.TEXTUAL.id,
                "Test Value 1",
                group.vid
            )

            with(
                handleRequest(
                    HttpMethod.Put,
                    "version/${version.id.value}/group/${group.id.value}/leaf/${leaf.id.value}"
                ) {
                    addHeader("Authorization", "Bearer $token")
                    addHeader("Content-Type", "application/json")
                    setBody(Json.encodeToString(dto))
                }
            ) {
                Assertions.assertEquals(HttpStatusCode.NoContent, response.status())
            }
        }
    }

    @Test
    fun `Test Update Leaf With Wrong Leaf Type`() {
        authorizedTest { user, token, transaction ->
            val version = transaction.createVersion(user)
            val group = transaction.createGroup(version)
            val leaf = transaction.createLeaf(version, group.vid)
            val dto = LeafUpdateDto(
                "Test Name 1",
                0,
                "Test Value 1",
                group.vid
            )

            with(
                handleRequest(
                    HttpMethod.Put,
                    "version/${version.id.value}/group/${group.id.value}/leaf/${leaf.id.value}"
                ) {
                    addHeader("Authorization", "Bearer $token")
                    addHeader("Content-Type", "application/json")
                    setBody(Json.encodeToString(dto))
                }
            ) {
                Assertions.assertEquals(HttpStatusCode.BadRequest, response.status())
                val response = Json.decodeFromString<List<String>>(response.content!!)
                assertTrue { 1 >= response.size }
            }
        }
    }
}
