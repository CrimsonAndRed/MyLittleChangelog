package my.little.changelog.routing

import io.ktor.config.*
import io.ktor.server.engine.*
import io.ktor.server.testing.*
import io.ktor.util.*
import my.little.changelog.configuration.auth.JwtConfig
import my.little.changelog.model.auth.User
import my.little.changelog.model.group.Group
import my.little.changelog.model.leaf.Leaf
import my.little.changelog.model.leaf.LeafType
import my.little.changelog.model.version.Version
import my.little.changelog.persistence.Db
import my.little.changelog.service.auth.AuthService
import org.jetbrains.exposed.sql.Schema
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.statements.api.ExposedBlob
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Testcontainers
import my.little.changelog.module as applicationModule
import my.little.changelog.persistence.module as persistenceModule
import my.little.changelog.routing.module as routingModule

@KtorExperimentalAPI
@Testcontainers
abstract class AbstractIntegrationTest {

    @BeforeEach
    fun createSchemas() {
        transaction {
            SchemaUtils.createSchema(Schema("public"), inBatch = true)
            Db.flyway.migrate()
        }
    }

    @AfterEach
    fun dropSchemas() {
        transaction {
            SchemaUtils.dropSchema(Schema("public"), cascade = true, inBatch = true)
        }
    }

    protected fun testApplication(test: TestApplicationEngine.() -> Unit) {
        Pg.engine.test()
    }

    protected fun authorizedTest(test: TestApplicationEngine.(User, String, Transaction) -> Unit) {
        testApplication {
            transaction {
                val user = this.createUser()
                val token = JwtConfig.makeToken(user)
                this@testApplication.test(user, token, this)
            }
        }
    }

    protected fun Transaction.createVersion(user: User, name: String = "Test version"): Version = Version
        .new {
            this.name = name
            this.user = user
        }.also { commit() }

    protected fun Transaction.createGroup(
        version: Version,
        parentVid: Int? = null,
        name: String = "Test Group"
    ): Group = Group
        .new {
            this.version = version
            this.parentVid = parentVid
            this.name = name
        }.also { commit() }

    protected fun Transaction.createLeaf(
        version: Version,
        groupVid: Int,
        name: String = "Test Leaf",
        valueType: Int = LeafType.TEXTUAL.id,
        value: String = "Test Value"
    ): Leaf = Leaf
        .new {
            this.version = version
            this.groupVid = groupVid
            this.name = name
            this.valueType = valueType
            this.value = value
        }.also { commit() }

    protected fun Transaction.createUser(login: String = "Test user", password: String = "password"): User = User
        .new {
            this.login = login
            this.password = ExposedBlob(AuthService.generateHash(password))
        }.also { commit() }

    companion object Pg {
        var pg: PostgreSQLContainer<Nothing>

        var engine: TestApplicationEngine

        init {
            // Initialize db first
            val pg = PostgreSQLContainer<Nothing>("postgres:13")
            pg.start()
            Pg.pg = pg

            // Then start application
            val e = TestApplicationEngine()
            e.start()
            (e.environment.config as MapApplicationConfig).apply {
                put("database.url", this@Pg.pg.jdbcUrl)
                put("database.username", this@Pg.pg.username)
                put("database.password", this@Pg.pg.password)

                put("jwt.audience", "test")
                put("jwt.issuer", "test")
                put("jwt.subject", "test")
                put("jwt.secret", "test")
                put("jwt.realm", "test")
            }
            e.application.applicationModule(testing = true)
            e.application.routingModule()
            e.application.persistenceModule()
            Pg.engine = e
        }

        @AfterAll
        fun stopEngine() {
            Pg.engine.stop(0, 0)
            pg.stop()
        }
    }
}
