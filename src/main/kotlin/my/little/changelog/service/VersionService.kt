package my.little.changelog.service

import my.little.changelog.model.version.Version
import my.little.changelog.model.version.dto.VersionDto
import my.little.changelog.model.version.toDto
import org.jetbrains.exposed.sql.transactions.transaction

fun createVersion(): VersionDto {
    return transaction {
        Version.new {
        }
    }.toDto()
}

fun getVersion(id: Int): VersionDto {
    return transaction {
        Version[id]
    }.toDto()
}
