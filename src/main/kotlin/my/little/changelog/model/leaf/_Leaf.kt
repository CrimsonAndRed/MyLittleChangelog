package my.little.changelog.model.leaf

import my.little.changelog.model.leaf.dto.external.LeafDto

fun Leaf.toExternal(): LeafDto = LeafDto(
    id = id.value,
    vid = vid,
    name = name,
    valueType = valueType,
    value = value,
)
