package my.little.changelog.persistence

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.application.Application

import io.ktor.config.*
import io.ktor.util.*

import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database
import javax.sql.DataSource

@KtorExperimentalAPI
fun Application.module() {
    val dataSource = initDb(environment.config.config("database"))
    initMigration(dataSource)
}

@KtorExperimentalAPI
fun initDb(conf: ApplicationConfig): DataSource {
    val hikariConfig = HikariConfig().apply {
        jdbcUrl = conf.property("url").getString()
        username = conf.property("username").getString()
        password = conf.property("password").getString()
    }
    val hikariDataSource = HikariDataSource(hikariConfig)
    Database.connect(hikariDataSource)
    return hikariDataSource
}

fun initMigration(dataSource: DataSource) {
    val flyway: Flyway = Flyway.configure().dataSource(dataSource).load()
    flyway.migrate()
}