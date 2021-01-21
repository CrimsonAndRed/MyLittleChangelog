package my.little.changelog.validator.dto

import my.little.changelog.model.leaf.dto.external.LeafCreationDto
import my.little.changelog.model.leaf.dto.external.LeafUpdateDto
import my.little.changelog.validator.ValidatorMessage
import my.little.changelog.validator.ValidatorResponse

object LeafDtoValidator {

    fun validateDtoNew(leaf: LeafCreationDto): ValidatorResponse {
        val errors: MutableList<String> = mutableListOf()
        leaf.name ?: errors.add(ValidatorMessage.isNull("name"))
        leaf.valueType ?: errors.add(ValidatorMessage.isNull("valueType"))
        leaf.value ?: errors.add(ValidatorMessage.isNull("value"))
        return ValidatorResponse(errors)
    }

    fun validateDtoUpdate(leaf: LeafUpdateDto): ValidatorResponse {
        val errors: MutableList<String> = mutableListOf()
        leaf.name ?: errors.add(ValidatorMessage.isNull("name"))
        leaf.valueType ?: errors.add(ValidatorMessage.isNull("valueType"))
        leaf.value ?: errors.add(ValidatorMessage.isNull("value"))
        return ValidatorResponse(errors)
    }
}
