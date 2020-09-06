package my.little.changelog.version

import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.handleRequest
import io.ktor.util.KtorExperimentalAPI
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import my.little.changelog.configuration.Json
import my.little.changelog.model.group.Group
import my.little.changelog.model.group.dto.GroupDto
import my.little.changelog.model.leaf.Leaf
import my.little.changelog.model.leaf.dto.LeafDto
import my.little.changelog.model.version.Version
import my.little.changelog.model.version.WholeVersion
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

@KtorExperimentalAPI
class VersionTest : AbstractIntegrationTest() {

    @Test
    fun test1() {
        testApplication {
            val version = transaction {
                Version.new {}
            }

            with(handleRequest(HttpMethod.Get, "/version?id=${version.id}")) {
                assertEquals(HttpStatusCode.OK, response.status())
            }
        }
    }

    @Test
    fun testGetWholeVersion() {
        testApplication {
            val version = transaction {
                val version = Version.new {}
                val group1 = Group.new {
                    this.vid = 1
                    this.version = version
                    this.name = "Группа1"
                    this.parentVid = null
                }

                val group2 = Group.new {
                    this.vid = 2
                    this.version = version
                    this.name = "Группа2"
                    this.parentVid = group1.vid
                }

                val leaf = Leaf.new {
                    this.vid = 1
                    this.name = "Значение1"
                    this.valueType = 1
                    this.value = "100"
                    this.version = version
                    this.groupVid = 1
                }



                val group2Dto = GroupDto(
                    id = group2.id.value,
                    vid = group2.vid,
                    version = group2.version.id.value,
                    name = group2.name,
                    groupContent = emptyList(),
                    leafContent = emptyList(),
                )

                val leafDto = LeafDto(
                    id = leaf.id.value,
                    vid = leaf.vid,
                    name = leaf.name,
                    valueType = leaf.valueType,
                    value = leaf.value,
                )

                val group1Dto = GroupDto(
                    id = group1.id.value,
                    vid = group1.vid,
                    version = group1.version.id.value,
                    name = group1.name,
                    groupContent = listOf(group2Dto),
                    leafContent = listOf(leafDto),
                )

                WholeVersion(
                    id = version.id.value,
                    groupContent = listOf(group1Dto),
                    leafContent = emptyList(),
                )
            }

            with(handleRequest(HttpMethod.Get, "/version?id=${version.id}")) {
                assertEquals(HttpStatusCode.OK, response.status())
                val json: WholeVersion = Json.decodeFromString(response.content!!)
                assertEquals(json, version)
            }
        }
    }
}
