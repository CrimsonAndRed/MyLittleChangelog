package my.little.changelog.persistence

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.application.Application
import io.ktor.config.*
import io.ktor.util.*
import org.jetbrains.exposed.sql.Database

@KtorExperimentalAPI
fun Application.module() {
    initDb(environment.config.config("database"))
}

@KtorExperimentalAPI
fun initDb(conf: ApplicationConfig) {
    val hikariConfig = HikariConfig().apply {
        jdbcUrl = conf.property("url").getString()
        username = conf.property("username").getString()
        password = conf.property("password").getString()
    }
    val hikariDataSource = HikariDataSource(hikariConfig)
    Database.connect(hikariDataSource)
}