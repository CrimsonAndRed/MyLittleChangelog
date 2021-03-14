package my.little.changelog.validator

import my.little.changelog.model.leaf.LeafType
import my.little.changelog.model.leaf.dto.service.LeafCreationDto
import my.little.changelog.model.leaf.dto.service.LeafUpdateDto
import java.math.BigDecimal

object LeafValidator {

    fun validateNew(leaf: LeafCreationDto): ValidatorResponse {
        val errors: MutableList<String> = mutableListOf()
        this.validateName(leaf.name, errors)
        this.validateLeafType(leaf.valueType, leaf.value, errors)

        return ValidatorResponse(errors)
    }

    fun validateUpdate(leaf: LeafUpdateDto): ValidatorResponse {
        val errors: MutableList<String> = mutableListOf()
        this.validateName(leaf.name, errors)
        this.validateLeafType(leaf.valueType, leaf.value, errors)

        return ValidatorResponse(errors)
    }

    private fun validateName(name: String, errors: MutableList<String>) {
        if (name.isBlank()) {
            errors.add(ValidatorMessage.isBlank("name"))
        }
    }

    private fun validateLeafType(type: Int, value: String, errors: MutableList<String>) {
        when (LeafType.get(type)) {
            LeafType.TEXTUAL -> {
                // No validation needed
            }
            LeafType.NUMERIC -> {
                try {
                    BigDecimal(value)
                } catch (e: NumberFormatException) {
                    errors.add("Incorrect value of given leaf type")
                }
            }
            null -> {
                errors.add("Incorrect leaf type")
            }
        }
    }
}
