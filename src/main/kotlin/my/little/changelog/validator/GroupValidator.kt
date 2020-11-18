package my.little.changelog.validator

import my.little.changelog.model.group.dto.service.GroupCreationDto
import my.little.changelog.model.group.dto.service.GroupUpdateDto

object GroupValidator {

    fun validateNew(group: GroupCreationDto): ValidatorResponse {
        val errors: MutableList<String> = mutableListOf()
        this.validateName(group.name, errors)

        return ValidatorResponse(errors)
    }

    fun validateUpdate(group: GroupUpdateDto): ValidatorResponse {
        val errors: MutableList<String> = mutableListOf()
        this.validateName(group.name, errors)

        return ValidatorResponse(errors)
    }

    private fun validateName(name: String, errors: MutableList<String>) {
        if (name.isBlank()) {
            errors.add(ValidatorMessage.isBlank("name"))
        }
    }
}
