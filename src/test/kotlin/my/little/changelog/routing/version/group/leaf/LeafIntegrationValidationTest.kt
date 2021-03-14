package my.little.changelog.routing.version.group.leaf

import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.util.KtorExperimentalAPI
import kotlinx.serialization.decodeFromString
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
    fun `Test Create Leaf With Blank Name Failure`() {
        authorizedTest { user, token, transaction ->
            val version = transaction.createVersion(user)
            val group = transaction.createGroup(version)
            val dto = LeafCreationDto(
                null,
                " ",
                LeafType.TEXTUAL.id,
                "Test Value 1"
            )

            testAuthorizedRequest(HttpMethod.Post, "version/${version.id.value}/group/${group.id.value}/leaf", token, dto) {
                Assertions.assertEquals(HttpStatusCode.BadRequest, response.status())
                val response = Json.decodeFromString<List<String>>(response.content!!)
                assertTrue { 1 >= response.size }
            }
        }
    }

    @Test
    fun `Test Create Leaf With Textual Type Success`() {
        authorizedTest { user, token, transaction ->
            val version = transaction.createVersion(user)
            val group = transaction.createGroup(version)
            val dto = LeafCreationDto(
                null,
                "Test Name",
                LeafType.TEXTUAL.id,
                "Test Value 1",
            )

            testAuthorizedRequest(
                HttpMethod.Post,
                "version/${version.id.value}/group/${group.id.value}/leaf",
                token,
                dto
            ) {
                Assertions.assertEquals(HttpStatusCode.OK, response.status())
            }
        }
    }

    @Test
    fun `Test Create Leaf With Numeric Type Success`() {
        authorizedTest { user, token, transaction ->
            val version = transaction.createVersion(user)
            val group = transaction.createGroup(version)
            val dto = LeafCreationDto(
                null,
                "Test Name",
                LeafType.NUMERIC.id,
                "60.03",
            )

            testAuthorizedRequest(
                HttpMethod.Post,
                "version/${version.id.value}/group/${group.id.value}/leaf",
                token,
                dto
            ) {
                Assertions.assertEquals(HttpStatusCode.OK, response.status())
            }
        }
    }

    @Test
    fun `Test Update Leaf With Blank Name Failure`() {
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

            testAuthorizedRequest(
                HttpMethod.Put,
                "version/${version.id.value}/group/${group.id.value}/leaf/${leaf.id.value}",
                token,
                dto
            ) {
                Assertions.assertEquals(HttpStatusCode.BadRequest, response.status())
                val response = Json.decodeFromString<List<String>>(response.content!!)
                assertTrue { 1 >= response.size }
            }
        }
    }

    @Test
    fun `Test Update Leaf With Textual Type Success`() {
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

            testAuthorizedRequest(
                HttpMethod.Put,
                "version/${version.id.value}/group/${group.id.value}/leaf/${leaf.id.value}",
                token,
                dto
            ) {
                Assertions.assertEquals(HttpStatusCode.NoContent, response.status())
            }
        }
    }

    @Test
    fun `Test Update Leaf With Wrong Leaf Type Failure`() {
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

            testAuthorizedRequest(
                HttpMethod.Put,
                "version/${version.id.value}/group/${group.id.value}/leaf/${leaf.id.value}",
                token,
                dto
            ) {
                Assertions.assertEquals(HttpStatusCode.BadRequest, response.status())
                val response = Json.decodeFromString<List<String>>(response.content!!)
                assertTrue { 1 >= response.size }
            }
        }
    }

    @Test
    fun `Test Update Leaf With Numeric Type Success`() {
        authorizedTest { user, token, transaction ->
            val version = transaction.createVersion(user)
            val group = transaction.createGroup(version)
            val leaf = transaction.createLeaf(version, group.vid)
            val dto = LeafUpdateDto(
                "Test Name 1",
                LeafType.NUMERIC.id,
                "1.100",
                group.vid
            )

            testAuthorizedRequest(
                HttpMethod.Put,
                "version/${version.id.value}/group/${group.id.value}/leaf/${leaf.id.value}",
                token,
                dto
            ) {
                Assertions.assertEquals(HttpStatusCode.NoContent, response.status())
            }
        }
    }

    @Test
    fun `Test Update Leaf With Numeric Type Incorrect Value Failure`() {
        authorizedTest { user, token, transaction ->
            val version = transaction.createVersion(user)
            val group = transaction.createGroup(version)
            val leaf = transaction.createLeaf(version, group.vid)
            val dto = LeafUpdateDto(
                "Test Name 1",
                LeafType.NUMERIC.id,
                "test",
                group.vid
            )

            testAuthorizedRequest(
                HttpMethod.Put,
                "version/${version.id.value}/group/${group.id.value}/leaf/${leaf.id.value}",
                token,
                dto
            ) {
                Assertions.assertEquals(HttpStatusCode.BadRequest, response.status())
            }
        }
    }
}
