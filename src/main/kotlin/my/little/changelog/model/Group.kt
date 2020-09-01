package my.little.changelog.model

data class Group(
        val id: Int,
        val vid: Int,
        val name: String,
        val parent_vid: Int,
        val version: Version
)