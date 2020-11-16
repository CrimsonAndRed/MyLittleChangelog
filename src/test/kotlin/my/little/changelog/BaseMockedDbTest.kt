package my.little.changelog

import io.ktor.util.KtorExperimentalAPI
import io.mockk.every
import io.mockk.mockk
import my.little.changelog.model.group.Group
import my.little.changelog.model.group.Groups
import my.little.changelog.model.version.Version
import my.little.changelog.model.version.Versions
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach

@KtorExperimentalAPI
abstract class BaseMockedDbTest : BaseTest() {

    @BeforeEach
    fun mockDb() {
        mockDatabase()
    }

    @AfterEach
    fun unmockDb() {
        unmockDatabase()
    }

    protected fun createVersion(id: Int) = Version(EntityID(id, Versions)).apply {
        this._readValues = ResultRow.createAndFillValues(emptyMap())
    }

    protected fun createGroup(id: Int, vid: Int, version: Version, name: String, parentVid: Int?) = Group(
        EntityID(id, Groups)
    ).apply {
        this._readValues = ResultRow.createAndFillValues(
            mapOf(
                Groups.vid to vid,
                Groups.version to version,
                Groups.name to name,
                Groups.parentVid to parentVid,
            )
        )
    }
}

class TestTransactionManager : TransactionManager {
    override var defaultIsolationLevel = 0
    override var defaultRepetitionAttempts = 0
    private val mockedDatabase: Database = mockk(relaxed = true)

    override fun currentOrNull(): Transaction? {
        return transaction()
    }

    private fun transaction(): Transaction {
        return mockk(relaxed = true) {
            every { db } returns mockedDatabase
        }
    }

    override fun newTransaction(isolation: Int, outerTransaction: Transaction?): Transaction {
        return transaction()
    }

    fun apply() {
        TransactionManager.registerManager(mockedDatabase, this@TestTransactionManager)
        Database.connect({ mockk(relaxed = true) }, { this })
    }

    fun reset() {
        TransactionManager.resetCurrent(null)
        TransactionManager.closeAndUnregister(mockedDatabase)
    }

    override fun bindTransactionToThread(transaction: Transaction?) {
    }
}

val manager = TestTransactionManager()
fun mockDatabase() = manager.apply()
fun unmockDatabase() = manager.reset()
