package my.little.changelog.routing.version.group.leaf

import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.TestApplicationEngine
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.setBody
import io.ktor.util.KtorExperimentalAPI
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import my.little.changelog.configuration.Json
import my.little.changelog.model.group.Group
import my.little.changelog.model.leaf.Leaf
import my.little.changelog.model.leaf.LeafType
import my.little.changelog.model.leaf.dto.external.ChangeLeafPositionDto
import my.little.changelog.model.leaf.dto.external.LeafCreationDto
import my.little.changelog.model.leaf.dto.external.LeafReturnedDto
import my.little.changelog.model.leaf.dto.external.LeafUpdateDto
import my.little.changelog.model.version.Version
import my.little.changelog.persistence.repo.LeafRepo
import my.little.changelog.routing.AbstractIntegrationTest
import org.jetbrains.exposed.dao.exceptions.EntityNotFoundException
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@KtorExperimentalAPI
internal class LeafIntegrationTest : AbstractIntegrationTest() {

    @Test
    fun `Test Create Leaf Success`() {
        testApplication {
            transaction {
                val version = createVersion()
                val group = createGroup(version)

                val dto = LeafCreationDto(
                    null,
                    "Test Name 1",
                    1,
                    "Test Value 1"
                )

                with(
                    handleRequest(HttpMethod.Post, "version/${version.id.value}/group/${group.id.value}/leaf") {
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
    }

    @Test
    fun `Test Create Leaf With Old Version`() {
        testApplication {
            transaction {
                val version = createVersion()
                createVersion()
                val group = createGroup(version)
                val dto = LeafCreationDto(
                    null,
                    "Test Name 1",
                    1,
                    "Test Value 1"
                )

                with(
                    handleRequest(HttpMethod.Post, "version/${version.id.value}/group/${group.id.value}/leaf") {
                        addHeader("Content-Type", "application/json")
                        setBody(Json.encodeToString(dto))
                    }
                ) {
                    assertEquals(HttpStatusCode.BadRequest, response.status())
                    val response = Json.decodeFromString<List<String>>(response.content!!)
                    assertTrue { 1 >= response.size }
                }
            }
        }
    }

    @Test
    fun `Test Create Leaf With Nonexistent Version`() {
        testApplication {
            transaction {
                val version = createVersion()
                val group = createGroup(version)
                val dto = LeafCreationDto(
                    null,
                    "Test Name 1",
                    LeafType.TEXTUAL.id,
                    "Test Value 1"
                )

                with(
                    handleRequest(HttpMethod.Post, "version/${version.id.value + 1}/group/${group.id.value}/leaf") {
                        addHeader("Content-Type", "application/json")
                        setBody(Json.encodeToString(dto))
                    }
                ) {
                    assertEquals(HttpStatusCode.InternalServerError, response.status())
                }
            }
        }
    }

    @Test
    fun `Test Create Leaf With Nonexistent Group`() {
        testApplication {
            transaction {
                val version = createVersion()
                val group = createGroup(version)
                val dto = LeafCreationDto(
                    null,
                    "Test Name 1",
                    LeafType.TEXTUAL.id,
                    "Test Value 1"
                )

                with(
                    handleRequest(HttpMethod.Post, "version/${version.id.value + 1}/group/${group.id.value + 1}/leaf") {
                        addHeader("Content-Type", "application/json")
                        setBody(Json.encodeToString(dto))
                    }
                ) {
                    assertEquals(HttpStatusCode.InternalServerError, response.status())
                }
            }
        }
    }

    @Test
    fun `Test Update Leaf Without Group Vid`() {
        testApplication {
            transaction {
                val version = createVersion()
                val group = createGroup(version)
                val leaf = createLeaf(version, group.vid)
                val dto = LeafUpdateDto(
                    "Test Name 1",
                    LeafType.TEXTUAL.id,
                    "Test Value 2",
                    group.vid
                )

                with(
                    handleRequest(HttpMethod.Put, "version/${version.id.value}/group/${group.id.value}/leaf/${leaf.id.value}") {
                        addHeader("Content-Type", "application/json")
                        setBody(Json.encodeToString(dto))
                    }
                ) {

                    assertEquals(HttpStatusCode.NoContent, response.status())
                }
            }
        }
    }

    @Test
    fun `Test Update Leaf With Group Vid`() {
        testApplication {
            transaction {
                val version = createVersion()
                val group = createGroup(version)
                val leaf = createLeaf(version, group.vid)
                val dto = LeafUpdateDto(
                    "Test Name 1",
                    LeafType.TEXTUAL.id,
                    "Значение1",
                    group.vid
                )

                with(
                    handleRequest(HttpMethod.Put, "version/${version.id.value}/group/${group.id.value}/leaf/${leaf.id.value}") {
                        addHeader("Content-Type", "application/json")
                        setBody(Json.encodeToString(dto))
                    }
                ) {
                    assertEquals(HttpStatusCode.NoContent, response.status())
                }
            }
        }
    }

    @Test
    fun `Test Update Leaf With Changing Group`() {
        testApplication {
            transaction {
                val version = createVersion()
                val group1 = createGroup(version)
                val group2 = createGroup(version)
                val leaf = createLeaf(version, group1.vid)

                val dto = LeafUpdateDto(
                    name = leaf.name,
                    valueType = leaf.valueType,
                    value = leaf.value,
                    parentVid = group2.vid,
                )

                with(
                    handleRequest(HttpMethod.Put, "version/${version.id.value}/group/${group1.id.value}/leaf/${leaf.id.value}") {
                        addHeader("Content-Type", "application/json")
                        setBody(Json.encodeToString(dto))
                    }
                ) {
                    assertEquals(HttpStatusCode.NoContent, response.status())
                }
            }
        }
    }

    @Test
    fun `Test Update Leaf With Nonexistent Version`() {
        testApplication {
            transaction {
                val version = createVersion()
                val group = createGroup(version)
                val leaf = createLeaf(version, group.vid)

                val dto = LeafUpdateDto(
                    "Лиф1",
                    1,
                    "Значение1",
                    group.vid
                )

                with(
                    handleRequest(HttpMethod.Put, "version/${version.id.value + 1}/group/${group.id.value}/leaf/${leaf.id.value}") {
                        addHeader("Content-Type", "application/json")
                        setBody(Json.encodeToString(dto))
                    }
                ) {
                    assertEquals(HttpStatusCode.NoContent, response.status())
                }
            }
        }
    }

    @Test
    fun `Test Update Leaf With Nonexistent Group`() {
        testApplication {
            transaction {
                val version = createVersion()
                val group = createGroup(version)
                val leaf = createLeaf(version, group.vid)
                val dto = LeafUpdateDto(
                    "Лиф1",
                    1,
                    "Значение1",
                    group.vid + 1
                )

                with(
                    handleRequest(HttpMethod.Put, "version/${version.id.value}/group/${group.id.value}/leaf/${leaf.id.value}") {
                        addHeader("Content-Type", "application/json")
                        setBody(Json.encodeToString(dto))
                    }
                ) {
                    assertEquals(HttpStatusCode.InternalServerError, response.status())
                }
            }
        }
    }

    @Test
    fun `Test Delete Leaf`() {
        testApplication {
            transaction {
                val version = createVersion()
                val group = createGroup(version)
                val leaf = createLeaf(version, group.vid)

                with(
                    handleRequest(HttpMethod.Delete, "version/${version.id.value}/group/${group.id.value}/leaf/${leaf.id.value}")
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
                val version1 = createVersion()
                val version2 = createVersion()
                val group = createGroup(version1)
                val leaf = createLeaf(version1, group.vid)

                with(
                    handleRequest(HttpMethod.Delete, "version/${version2.id.value}/group/${group.id.value}/leaf/${leaf.id.value}")
                ) {
                    assertEquals(HttpStatusCode.BadRequest, response.status())
                }
                assertNotNull(Leaf[leaf.id])
            }
        }
    }

    @Test
    fun `Test Delete Nonexistent Leaf`() {
        testApplication {
            transaction {
                val version1 = createVersion()
                val group = createGroup(version1)
                val leaf = createLeaf(version1, group.vid)

                with(
                    handleRequest(HttpMethod.Delete, "version/${version1.id.value}/group/${group.id.value}/leaf/${leaf.id.value + 1}")
                ) {
                    assertEquals(HttpStatusCode.InternalServerError, response.status())
                }
                assertNotNull(Leaf[leaf.id])
            }
        }
    }

    @Test
    fun `Test Move leaf`() {
        testApplication {
            transaction {
                val version = createVersion()
                val group = createGroup(version)
                val leaf1 = createLeaf(version, group.vid)
                val leaf2 = createLeaf(version, group.vid)
                testMoveLeafPositive(version, group, leaf1, leaf2)
            }
            transaction {
                val version = createVersion()
                val group = createGroup(version)
                val leaf1 = createLeaf(version, group.vid)
                val leaf2 = createLeaf(version, group.vid)
                testMoveLeafPositive(version, group, leaf2, leaf1)
            }
            transaction {
                val version = createVersion()
                val group = createGroup(version)
                val group2 = createGroup(version)
                val leaf1 = createLeaf(version, group.vid)
                val leaf2 = createLeaf(version, group2.vid)
                testMoveLeafNegative(version, group, leaf2, leaf1)
            }
        }
    }

    private fun TestApplicationEngine.testMoveLeafPositive(version: Version, group: Group, leaf1: Leaf, leaf2: Leaf) {
        val dto = ChangeLeafPositionDto(leaf2.id.value)
        handleRequest(HttpMethod.Patch, "/version/${version.id}/group/${group.id}/leaf/${leaf1.id}/position") {
            addHeader("Content-Type", "application/json")
            setBody(Json.encodeToString(dto))
        }.apply {
            assertEquals(HttpStatusCode.NoContent, response.status())
            assertEquals(LeafRepo.findById(leaf1.id.value).order, leaf2.order)
            assertEquals(LeafRepo.findById(leaf2.id.value).order, leaf1.order)
        }
    }

    private fun TestApplicationEngine.testMoveLeafNegative(version: Version, group: Group, leaf1: Leaf, leaf2: Leaf) {
        val dto = ChangeLeafPositionDto(leaf2.id.value)
        handleRequest(HttpMethod.Patch, "/version/${version.id}/group/${group.id}/leaf/${leaf1.id}/position") {
            addHeader("Content-Type", "application/json")
            setBody(Json.encodeToString(dto))
        }.apply {
            assertEquals(HttpStatusCode.BadRequest, response.status())
        }
    }
}
