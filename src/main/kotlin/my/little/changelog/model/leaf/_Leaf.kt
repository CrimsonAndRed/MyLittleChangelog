package my.little.changelog.model.leaf

import my.little.changelog.model.leaf.dto.external.LeafDto

fun Leaf.toExternalDto() = LeafDto(
    id = id.value,
    vid = vid,
    name = name,
    valueType = valueType,
    value = value,
)
