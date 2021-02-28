package my.little.changelog.model.leaf

import my.little.changelog.model.leaf.dto.service.LeafReturnedDto

fun Leaf.toReturnedDto() = LeafReturnedDto(
    id = id.value,
    vid = vid,
    name = name,
    valueType = valueType,
    value = value,
    groupVid = groupVid!!
)
