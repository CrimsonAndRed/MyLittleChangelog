package my.little.changelog.persistence.group

import my.little.changelog.model.version.Version
import org.jetbrains.exposed.sql.transactions.transaction

object VersionRepo {

    fun createVersion(): Version = transaction{
        Version.new {  }
    }

    fun findVersionById(id: Int): Version = transaction {
        Version[id]
    }
}