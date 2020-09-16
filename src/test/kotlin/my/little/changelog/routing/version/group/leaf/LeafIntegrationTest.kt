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
import my.little.changelog.model.leaf.dto.external.LeafReturnedDto
import my.little.changelog.model.leaf.dto.external.LeafUpdateDto
import my.little.changelog.model.leaf.dto.external.WholeLeafDto
import my.little.changelog.model.version.Version
import my.little.changelog.routing.AbstractIntegrationTest
import org.jetbrains.exposed.dao.exceptions.EntityNotFoundException
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertNotNull

@KtorExperimentalAPI
internal class LeafIntegrationTest : AbstractIntegrationTest() {

    @Test
    fun testCreateLeaf() {
        testApplication {
            val (version, group) = transaction {
                val version = Version.new {}
                val group = Group.new {
                    // TODO(#3) почему 1
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

            with(
                handleRequest(HttpMethod.Post, "/version/${version.id}/group/${group.id}/leaf") {
                    addHeader("Content-Type", "application/json")
                    setBody(Json.encodeToString(dto))
                }
            ) {

                assertEquals(HttpStatusCode.OK, response.status())
                val response = Json.decodeFromString<LeafReturnedDto>(response.content!!)
                assertEquals(dto.name, response.name)
                assertEquals(dto.value, response.value)
                assertEquals(dto.valueType, response.valueType)
            }
        }
    }

    @Test
    fun testUpdateLeafWithoutGroupVid() {
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
                    "Имя1",
                    2,
                    "Значение 2",
                    group.vid
                )

                with(
                    handleRequest(HttpMethod.Put, "/version/${version.id}/group/${group.id}/leaf/${leaf.id}") {
                        addHeader("Content-Type", "application/json")
                        setBody(Json.encodeToString(dto))
                    }
                ) {

                    assertEquals(HttpStatusCode.OK, response.status())
                    val response: WholeLeafDto = Json.decodeFromString(response.content!!)
                    assertEquals(leaf.id.value, response.id)
                    assertEquals(leaf.vid, response.vid)
                    assertEquals(dto.name, response.name)
                    assertEquals(dto.value, response.value)
                    assertEquals(dto.valueType, response.valueType)
                }
            }
        }
    }

    @Test
    fun testUpdateLeafWithGroupVid() {
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
                    "Лиф1",
                    1,
                    "Значение1"
                )

                with(
                    handleRequest(HttpMethod.Put, "/version/${version.id}/group/${group.id}/leaf/${leaf.id}") {
                        addHeader("Content-Type", "application/json")
                        setBody(Json.encodeToString(dto))
                    }
                ) {
                    assertEquals(HttpStatusCode.OK, response.status())

                    val response: WholeLeafDto = Json.decodeFromString(response.content!!)
                    assertEquals(leaf.id.value, response.id)
                    assertEquals(leaf.vid, response.vid)
                    assertEquals(dto.name, response.name)
                    assertEquals(dto.value, response.value)
                    assertEquals(dto.valueType, response.valueType)
                }
            }
        }
    }

    @Test
    fun `Test Delete Leaf`() {
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

                with(
                    handleRequest(HttpMethod.Delete, "/version/${version.id}/group/${group.id}/leaf/${leaf.id}")
                ) {
                    assertEquals(HttpStatusCode.NoContent, response.status())
                }
                assertThrows<EntityNotFoundException> { Leaf[leaf.id] }
            }
        }
    }

    @Test
    fun `Test Delete Leaf with Wrong Version`() {
        testApplication {
            transaction {
                val version1 = Version.new {}
                val version2 = Version.new {}
                val group = Group.new {
                    this.version = version1
                    this.name = "Группа1"
                    this.parentVid = null
                }
                commit()

                val leaf = Leaf.new {
                    this.valueType = 1
                    this.value = "Значение1"
                    this.version = version1
                    this.name = "Лиф1"
                    this.groupVid = group.vid
                }
                commit()

                with(
                    handleRequest(HttpMethod.Delete, "/version/${version2.id}/group/${group.id}/leaf/${leaf.id}")
                ) {
                    assertEquals(HttpStatusCode.InternalServerError, response.status())
                }
                assertNotNull(Leaf[leaf.id])
            }
        }
    }
}
