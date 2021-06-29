package my.little.changelog.validator.dto

import my.little.changelog.model.project.dto.external.ProjectCreationDto
import my.little.changelog.validator.ValidatorMessage
import my.little.changelog.validator.ValidatorResponse

object ProjectDtoValidator {

    fun validateNew(project: ProjectCreationDto): ValidatorResponse {
        val errors: MutableList<String> = mutableListOf()
        validateName(project.name, errors)
        validateDescription(project.description, errors)

        return ValidatorResponse(errors)
    }

    private fun validateDescription(description: String?, errors: MutableList<String>) {
        if (description == null || description.isBlank()) {
            errors.add(ValidatorMessage.isBlank("name"))
        }
    }

    private fun validateName(name: String?, errors: MutableList<String>) {
        if (name == null || name.isBlank()) {
            errors.add(ValidatorMessage.isBlank("description"))
        }
    }
}
