package my.little.changelog.model

import org.jetbrains.exposed.sql.Table

data class Group(
    val id: Int,
    val vid: Int,
    val name: String,
    val parentVid: Int,
    val version: Version
)

object Groups : Table() {
    val id = integer("id").autoIncrement()
    val vid = integer("vid")
    val name = text("name")
    val parentVid = integer("parent_vid")
    val version = (integer("version_id") references Versions.id).nullable()

    override val primaryKey = PrimaryKey(id)
}
