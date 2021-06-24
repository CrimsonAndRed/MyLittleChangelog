package my.little.changelog.model.leaf

enum class LeafType(val id: Int) {
    TEXTUAL(1),
    NUMERIC(2);

    companion object {
        private val map: Map<Int, LeafType> = LeafType.values().map { it.id to it }.toMap()
        fun valuesById(): Map<Int, LeafType> {
            return map
        }

        fun get(id: Int): LeafType? {
            return map[id]
        }
    }
}
