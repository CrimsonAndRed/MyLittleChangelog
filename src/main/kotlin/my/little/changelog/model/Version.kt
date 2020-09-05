package my.little.changelog.model

import org.jetbrains.exposed.sql.Table

data class Version(
        val id: Int,
)

object Versions : Table() {
    val id = integer("id").autoIncrement()

    override val primaryKey = PrimaryKey(id)
}