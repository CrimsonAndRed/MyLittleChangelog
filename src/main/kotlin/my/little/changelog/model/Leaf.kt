package my.little.changelog.model

data class Leaf(
        val id: Int,
        val vid: Int,
        val name: String,
        val valueType: Int,
        val value: String,
        val version: Version,
        val group_vid: Int
)