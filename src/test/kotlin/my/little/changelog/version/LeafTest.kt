package my.little.changelog.version

import io.ktor.http.HttpMethod
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.setBody
import io.ktor.util.KtorExperimentalAPI
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import my.little.changelog.configuration.Json
import my.little.changelog.model.group.Group
import my.little.changelog.model.leaf.dto.external.LeafCreationDto
import my.little.changelog.model.leaf.dto.external.LeafDto
import my.little.changelog.model.version.Version
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

@KtorExperimentalAPI
class LeafTest : AbstractIntegrationTest() {

    @Test
    fun testCreateLeaf() {
        testApplication {
            val (version, group) = transaction {
                val version = Version.new {}
                val group = Group.new {
                    this.vid = 1
                    this.version = version
                    this.name = "Группа1"
                    this.parentVid = null
                }

                version to group
            }
            val dto = LeafCreationDto(
                "Имя1",
                1,
                "Значение1"
            )

            with(handleRequest(HttpMethod.Post, "/version/${version.id}/group/${group.id}/leaf") {
                addHeader("Content-Type", "application/json")
                setBody(Json.encodeToString(dto))
            }) {

                assertEquals(io.ktor.http.HttpStatusCode.OK, response.status())
                val json: LeafDto = Json.decodeFromString(response.content!!)
                assertEquals(json.name, dto.name)
                assertEquals(json.value, dto.value)
                assertEquals(json.valueType, dto.valueType)
            }
        }

    }
}