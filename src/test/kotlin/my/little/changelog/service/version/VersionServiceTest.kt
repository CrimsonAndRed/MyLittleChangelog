package my.little.changelog.service.version

import io.ktor.util.KtorExperimentalAPI
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.mockkStatic
import my.little.changelog.BaseMockedDbTest
import my.little.changelog.model.group.Group
import my.little.changelog.model.leaf.Leaf
import my.little.changelog.model.version.Version
import my.little.changelog.model.version.Versions
import my.little.changelog.persistence.repo.GroupRepo
import my.little.changelog.persistence.repo.LeafRepo
import my.little.changelog.persistence.repo.VersionRepo
import my.little.changelog.service.VersionService
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.experimental.categories.Category
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

@KtorExperimentalAPI
internal class VersionServiceTest : BaseMockedDbTest() {

    init {
        mockkObject(VersionRepo)
        mockkObject(GroupRepo)
        mockkObject(LeafRepo)

        mockkStatic("org.jetbrains.exposed.sql.transactions.ThreadLocalTransactionManagerKt")
        every { transaction(mockk(relaxed = true), any<Transaction.() -> List<Category>>()) } answers {
            val execFunction: Transaction.() -> List<Category> = secondArg()
            mockk<Transaction>(relaxed = true).execFunction()
        }
    }

    @Test
    fun `Test Create Version Success`() = testApplication {
        val model = Version(id = EntityID(0, Versions))
        every { VersionRepo.create(any()) } returns model

        val resp = VersionService.createVersion()

        assertEquals(model.id.value, resp.id)
    }

    @Test
    fun `Test Create Version Exception`() = testApplication {
        every { VersionRepo.create(any()) } throws RuntimeException()

        assertThrows<RuntimeException> {
            VersionService.createVersion()
        }
    }

    @Test
    fun `Test Get WholeVersion 1`() = testApplication {
        transaction {

            val version1 = Version.new {}

            val group1 = Group.new {
                this.vid = 0
                this.version = version1
                this.name = "Test 1"
                this.parentVid = null
            }

            val leaf1 = Leaf.new {
                this.vid = 0
                this.name = "Test 1"
                this.valueType = 1
                this.value = "Значение 1"
                this.groupVid = group1.vid
            }

            commit()

            every { VersionRepo.findById(any()) } returns version1
            every { LeafRepo.findByVersion(any()) } returns listOf(leaf1)
            every { GroupRepo.findGroupAffectedByVersion(any()) } returns listOf(group1)

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
}
