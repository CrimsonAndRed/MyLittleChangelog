package my.little.changelog.persistence

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.application.*
import io.ktor.config.*
import org.flywaydb.core.Flyway
import org.flywaydb.core.api.Location
import org.jetbrains.exposed.sql.Database
import javax.sql.DataSource

fun Application.module() {
    val dataSource = initDb(environment.config.config("database"))
    Db.source = dataSource
    initMigration(dataSource)
}

fun initDb(conf: ApplicationConfig): DataSource {
    val hikariConfig = HikariConfig().apply {
        jdbcUrl = conf.property("url").getString()
        username = conf.property("username").getString()
        password = conf.property("password").getString()
    }
    val hikariDataSource = HikariDataSource(hikariConfig)
    Database.connect(hikariDataSource).apply {
        useNestedTransactions = true
    }
    return hikariDataSource
}

fun initMigration(dataSource: DataSource) {
    val flyway: Flyway = Flyway.configure().dataSource(dataSource).locations(Location("classpath:/db/migration")).load()
    Db.flyway = flyway
    flyway.migrate()
}

object Db {
    lateinit var source: DataSource
    lateinit var flyway: Flyway
}
