package my.little.changelog.routing.version.group.leaf

import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.setBody
import io.ktor.util.KtorExperimentalAPI
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import my.little.changelog.configuration.Json
import my.little.changelog.model.group.Group
import my.little.changelog.model.leaf.Leaf
import my.little.changelog.model.leaf.dto.external.LeafCreationDto
import my.little.changelog.model.leaf.dto.external.LeafUpdateDto
import my.little.changelog.model.version.Version
import my.little.changelog.routing.AbstractIntegrationTest
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

@KtorExperimentalAPI
internal class GroupIntegrationValidationTest : AbstractIntegrationTest() {
    @Test
    fun `Test Create Leaf With Blank Name`() {
        testApplication {
            transaction {
                val version = Version.new {}
                val group = Group.new {
                    this.version = version
                    this.name = "Группа1"
                    this.parentVid = null
                }
                commit()

                val dto = LeafCreationDto(
                    null,
                    " ",
                    1,
                    "Значение1"
                )

                with(
                    handleRequest(HttpMethod.Post, "version/${version.id.value}/group/${group.id.value}/leaf") {
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

    @Test
    fun `Test Update Leaf With Blank Name`() {
        testApplication {
            transaction {
                val version = Version.new {}
                val group = Group.new {
                    this.version = version
                    this.name = "Группа1"
                    this.parentVid = null
                }
                commit()
                val leaf = Leaf.new {
                    this.valueType = 1
                    this.value = "Значение1"
                    this.version = version
                    this.name = "Лиф1"
                    this.groupVid = group.vid
                }
                commit()

                val dto = LeafUpdateDto(
                    " ",
                    1,
                    "Значение1",
                    group.vid
                )

                with(
                    handleRequest(HttpMethod.Put, "version/${version.id.value}/group/${group.id.value}/leaf/${leaf.id.value}") {
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
}
