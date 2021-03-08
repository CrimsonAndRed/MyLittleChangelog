package my.little.changelog.validator.dto

import my.little.changelog.model.auth.dto.external.AuthDto
import my.little.changelog.model.auth.dto.external.UserCreationDto
import my.little.changelog.validator.ValidatorMessage
import my.little.changelog.validator.ValidatorResponse

object AuthDtoValidator {

    fun validateAuth(dto: AuthDto): ValidatorResponse {

        val errors: MutableList<String> = mutableListOf()
        if (dto.login.isBlank()) {
            errors.add(ValidatorMessage.isBlank("login"))
        }

        if (dto.password.isBlank()) {
            errors.add(ValidatorMessage.isBlank("password"))
        }
        return ValidatorResponse(errors)
    }

    fun validateNewUser(dto: UserCreationDto): ValidatorResponse {

        val errors: MutableList<String> = mutableListOf()
        if (dto.login.isBlank()) {
            errors.add(ValidatorMessage.isBlank("login"))
        }

        if (dto.password.isBlank()) {
            errors.add(ValidatorMessage.isBlank("password"))
        }
        return ValidatorResponse(errors)
    }
}
