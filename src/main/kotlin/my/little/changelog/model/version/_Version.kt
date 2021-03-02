package my.little.changelog.model.version

import my.little.changelog.model.version.dto.service.ReturnedVersionDto

fun Version.toReturnedDto() = ReturnedVersionDto(
    id = this.id.value,
    name = this.name,
    order = this.order
)
