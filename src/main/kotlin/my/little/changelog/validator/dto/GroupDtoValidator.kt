package my.little.changelog.validator.dto

import my.little.changelog.model.group.dto.external.GroupCreationDto
import my.little.changelog.model.group.dto.external.GroupUpdateDto
import my.little.changelog.validator.ValidatorMessage
import my.little.changelog.validator.ValidatorResponse

object GroupDtoValidator {

    fun validateDtoNew(group: GroupCreationDto): ValidatorResponse {
        val errors: MutableList<String> = mutableListOf()
        group.name ?: errors.add(ValidatorMessage.isNull("name"))
        return ValidatorResponse(errors)
    }

    fun validateDtoUpdate(group: GroupUpdateDto): ValidatorResponse {
        val errors: MutableList<String> = mutableListOf()
        group.name ?: errors.add(ValidatorMessage.isNull("name"))
        return ValidatorResponse(errors)
    }
}
