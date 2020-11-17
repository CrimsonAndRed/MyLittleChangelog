package my.little.changelog.validator

import my.little.changelog.model.leaf.LeafType
import my.little.changelog.model.leaf.dto.service.LeafCreationDto
import my.little.changelog.model.leaf.dto.service.LeafUpdateDto

object LeafValidator {

    fun validateNew(leaf: LeafCreationDto): ValidatorResponse {
        val errors: MutableList<String> = mutableListOf()
        this.validateName(leaf.name, errors)
        this.validateLeafType(leaf.valueType, errors)

        return ValidatorResponse(errors)
    }

    fun validateUpdate(leaf: LeafUpdateDto): ValidatorResponse {
        val errors: MutableList<String> = mutableListOf()
        this.validateName(leaf.name, errors)
        this.validateLeafType(leaf.valueType, errors)

        return ValidatorResponse(errors)
    }

    private fun validateName(name: String, errors: MutableList<String>) {
        if (name.isBlank()) {
            errors.add("Name can not be blank")
        }
    }

    private fun validateLeafType(leafType: Int, errors: MutableList<String>) {
        if (LeafType.get(leafType) == null) {
            errors.add("Incorrect leaf type")
        }
    }
}
