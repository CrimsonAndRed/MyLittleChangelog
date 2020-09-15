package my.little.changelog.service.version

import io.ktor.util.KtorExperimentalAPI
import io.mockk.every
import io.mockk.mockkObject
import my.little.changelog.BaseMockedDbTest
import my.little.changelog.model.group.Group
import my.little.changelog.model.group.Groups
import my.little.changelog.model.leaf.Leaf
import my.little.changelog.model.leaf.Leaves
import my.little.changelog.model.version.Version
import my.little.changelog.model.version.Versions
import my.little.changelog.persistence.repo.GroupRepo
import my.little.changelog.persistence.repo.LeafRepo
import my.little.changelog.persistence.repo.VersionRepo
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.ResultRow
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

@KtorExperimentalAPI
internal class VersionServiceTest : BaseMockedDbTest() {

    init {
        mockkObject(VersionRepo)
        mockkObject(GroupRepo)
        mockkObject(LeafRepo)
    }

    @Test
    fun `Test Create Version Success`() {
        val model = Version(id = EntityID(0, Versions))
        every { VersionRepo.create(any()) } returns model

        val resp = VersionService.createVersion()

        assertEquals(model.id.value, resp.id)
    }

    @Test
    fun `Test Create Version Exception`() {
        every { VersionRepo.create(any()) } throws RuntimeException()

        assertThrows<RuntimeException> {
            VersionService.createVersion()
        }
    }

    @Test
    fun `Test Get WholeVersion 1`() {

        val version1 = Version(EntityID(0, Versions)).apply {
            this._readValues = ResultRow.createAndFillValues(mapOf())
        }

        val group1 = Group(EntityID(0, Groups)).apply {
            this._readValues = ResultRow.createAndFillValues(
                mapOf(
                    Groups.vid to 0,
                    Groups.version to version1,
                    Groups.name to "Test 1",
                    Groups.parentVid to null,
                )
            )
        }

        val leaf1 = Leaf(EntityID(0, Leaves)).apply {
            this._readValues = ResultRow.createAndFillValues(
                mapOf(
                    Leaves.vid to 0,
                    Leaves.version to version1,
                    Leaves.name to "Test 1",
                    Leaves.groupVid to group1.vid,
                    Leaves.valueType to 1,
                    Leaves.value to "Значение 1",
                )
            )
        }

        every { VersionRepo.findById(allAny()) } returns version1
        every { LeafRepo.findByVersion(allAny()) } returns listOf(leaf1)
        every { GroupRepo.findGroupAffectedByVersion(allAny()) } returns listOf(group1)

        val wv = VersionService.getWholeVersion(version1.id.value)

        assertEquals(version1.id.value, wv.id)
        assertEquals(1, wv.groupContent.size)
        val subgroup = wv.groupContent[0]

        assertEquals(group1.id.value, subgroup.id)
        assertEquals(group1.vid, subgroup.vid)
        assertEquals(group1.name, subgroup.name)
        assertEquals(0, subgroup.groupContent.size)
        assertEquals(1, subgroup.leafContent.size)

        val leaf = subgroup.leafContent[0]

        assertEquals(leaf1.id.value, leaf.id)
        assertEquals(leaf1.vid, leaf.vid)
        assertEquals(leaf1.name, leaf.name)
        assertEquals(leaf1.value, leaf.value)
        assertEquals(leaf1.valueType, leaf.valueType)

        assertEquals(0, wv.leafContent.size)
    }
}
