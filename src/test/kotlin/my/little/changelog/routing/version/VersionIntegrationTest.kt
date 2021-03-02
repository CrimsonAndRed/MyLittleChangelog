package my.little.changelog.routing.version

import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.handleRequest
import io.ktor.util.KtorExperimentalAPI
import kotlinx.serialization.decodeFromString
import my.little.changelog.configuration.Json
import my.little.changelog.model.group.dto.external.WholeGroupDto
import my.little.changelog.model.leaf.dto.external.WholeLeafDto
import my.little.changelog.model.version.dto.external.PreviousVersionsDTO
import my.little.changelog.model.version.dto.external.ReturnedVersionDto
import my.little.changelog.model.version.dto.external.WholeVersion
import my.little.changelog.persistence.repo.GroupRepo
import my.little.changelog.persistence.repo.LeafRepo
import my.little.changelog.persistence.repo.VersionRepo
import my.little.changelog.routing.AbstractIntegrationTest
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test

@KtorExperimentalAPI
internal class VersionIntegrationTest : AbstractIntegrationTest() {

    @Test
    fun `Test Create Version Success`() {
        testApplication {
            transaction {
                val version = createVersion()

                with(handleRequest(HttpMethod.Get, "version/${version.id.value}")) {
                    assertEquals(HttpStatusCode.OK, response.status())
                }
            }
        }
    }

    @Test
    fun `Test Get Version Success`() {
        testApplication {
            val version = transaction {

                val version = createVersion()
                val group1 = createGroup(version)
                val group2 = createGroup(version, group1.vid)
                val leaf = createLeaf(version, group1.vid)

                val group2Dto = WholeGroupDto(
                    id = group2.id.value,
                    vid = group2.vid,
                    name = group2.name,
                    realNode = true,
                    groupContent = emptyList(),
                    leafContent = emptyList(),
                    isEarliest = true,
                )

                val leafDto = WholeLeafDto(
                    id = leaf.id.value,
                    vid = leaf.vid,
                    name = leaf.name,
                    valueType = leaf.valueType,
                    value = leaf.value,
                )

                val group1Dto = WholeGroupDto(
                    id = group1.id.value,
                    vid = group1.vid,
                    name = group1.name,
                    realNode = true,
                    groupContent = listOf(group2Dto),
                    leafContent = listOf(leafDto),
                    isEarliest = true,
                )

                WholeVersion(
                    id = version.id.value,
                    canChange = true,
                    groupContent = listOf(group1Dto),
                )
            }

            with(handleRequest(HttpMethod.Get, "version/${version.id}")) {
                assertEquals(HttpStatusCode.OK, response.status())
                val json: WholeVersion = Json.decodeFromString(response.content!!)
                assertEquals(json, version)
            }
        }
    }

    @Test
    fun `Test Get Versions Success`() {
        testApplication {
            transaction {
                createVersion()
                createVersion()

                with(handleRequest(HttpMethod.Get, "version")) {
                    assertEquals(HttpStatusCode.OK, response.status())
                    val jsonList: List<ReturnedVersionDto> = Json.decodeFromString(response.content!!)
                    assertEquals(2, jsonList.size)
                }
            }
        }
    }

    @Test
    fun `Test Delete Version Success`() {
        testApplication {
            transaction {
                val firstVersion = createVersion()
                val latestVersion = createVersion()
                val g1 = createGroup(latestVersion)
                createGroup(latestVersion, g1.vid)
                createLeaf(latestVersion, g1.vid)

                with(handleRequest(HttpMethod.Delete, "version/${latestVersion.id.value}")) {
                    transaction {
                        assertEquals(HttpStatusCode.NoContent, response.status())
                        assertEquals(firstVersion.id, VersionRepo.findLatest().id)
                        assertEquals(0, GroupRepo.findAll().count())
                        assertEquals(0, LeafRepo.findAll().count())
                    }
                }
            }
        }
    }

    @Test
    fun `Test Delete Not Latest Version Failure`() {
        testApplication {
            transaction {
                val firstVersion = createVersion()
                val latestVersion = createVersion()
                val group = createGroup(latestVersion)
                createLeaf(latestVersion, group.vid)

                with(handleRequest(HttpMethod.Delete, "version/${firstVersion.id.value}")) {
                    transaction {
                        assertEquals(HttpStatusCode.BadRequest, response.status())
                        assertNotEquals(firstVersion, VersionRepo.findLatest())
                        assertEquals(1, GroupRepo.findAll().count())
                        assertEquals(1, LeafRepo.findAll().count())
                    }
                }
            }
        }
    }

    @Test
    fun `Test Delete Nonexistent Version Failure`() {
        testApplication {
            transaction {
                val firstVersion = createVersion()
                createGroup(firstVersion)

                with(handleRequest(HttpMethod.Delete, "version/${firstVersion.id.value + 1}")) {
                    transaction {
                        assertEquals(HttpStatusCode.InternalServerError, response.status())
                    }
                }
            }
        }
    }

    @Test
    fun `Test Get Previous Version Success`() {
        testApplication {
            transaction {
                val latestVersion = createVersion()
                val group = createGroup(latestVersion)
                val leaf = createLeaf(latestVersion, group.vid)
                with(handleRequest(HttpMethod.Get, "version/previous")) {
                    transaction {
                        assertEquals(HttpStatusCode.OK, response.status())
                        val json: PreviousVersionsDTO = Json.decodeFromString(response.content!!)
                        assertEquals(1, json.groupContent.size)
                        assertEquals(1, json.groupContent[0].leafContent.size)

                        val groupContent = json.groupContent[0]

                        assertEquals(groupContent.id, group.id.value)
                        assertEquals(groupContent.vid, group.vid)
                        assertEquals(groupContent.name, group.name)
                        assertEquals(groupContent.groupContent.size, 0)
                        assertEquals(groupContent.leafContent.size, 1)

                        val leafContent = json.groupContent[0].leafContent[0]

                        assertEquals(leafContent.id, leaf.id.value)
                        assertEquals(leafContent.vid, leaf.vid)
                        assertEquals(leafContent.name, leaf.name)
                        assertEquals(leafContent.valueType, leaf.valueType)
                        assertEquals(leafContent.value, leaf.value)
                    }
                }
            }
        }
    }

    @Test
    fun `Test Get Versions Preserve Order Success`() {
        testApplication {
            transaction {
                val v1 = createVersion()
                val v2 = createVersion()

                with(handleRequest(HttpMethod.Get, "version")) {
                    assertEquals(HttpStatusCode.OK, response.status())
                    val jsonList: List<ReturnedVersionDto> = Json.decodeFromString(response.content!!)
                    assertEquals(2, jsonList.size)
                    assertEquals(v1.id.value, jsonList[0].id)
                    assertEquals(v2.id.value, jsonList[1].id)
                }
            }
        }
    }
}
