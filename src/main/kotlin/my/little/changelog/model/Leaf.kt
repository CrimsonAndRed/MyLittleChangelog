package my.little.changelog.model

import org.jetbrains.exposed.sql.Table

data class Leaf(
    val id: Int,
    val vid: Int,
    val name: String,
    val valueType: Int,
    val value: String,
    val version: Version,
    val groupVid: Int
)

object Leaves : Table() {
    val id = integer("id").autoIncrement()
    val vid = integer("vid")
    val name = text("name")
    val valueType = integer("value_type")
    val value = text("value")
    val versionId = (integer("version_id") references Versions.id).nullable()
    val groupVid = integer("group_vid")

    override val primaryKey = PrimaryKey(id)
}
