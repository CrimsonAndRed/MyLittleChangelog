package my.little.changelog.validator

import my.little.changelog.exception.ForbiddenException
import my.little.changelog.model.auth.User
import my.little.changelog.persistence.repo.AuthRepo

object AuthValidator {

    @Throws(ForbiddenException::class)
    fun validateAuthority(expectedUser: User, currentUser: User): ValidatorResponse {
        if (expectedUser.id.value != currentUser.id.value) {
            throw ForbiddenException()
        }
        return ValidatorResponse(emptyList())
    }

    fun validateSameLogin(login: String): ValidatorResponse {
        val count = AuthRepo.countByLogin(login)
        return if (count > 0) {
            ValidatorResponse(listOf("User with same login exists"))
        } else {
            ValidatorResponse(emptyList())
        }
    }
}
