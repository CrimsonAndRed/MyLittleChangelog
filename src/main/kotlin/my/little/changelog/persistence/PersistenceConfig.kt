package my.little.changelog.persistence

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.application.Application
import org.jetbrains.exposed.sql.Database

fun Application.module() {
    initDb()
}

fun initDb() {
    val hikariConfig = HikariConfig("/hikari.properties")
    val hikariDataSource = HikariDataSource(hikariConfig)
    Database.connect(hikariDataSource)
}