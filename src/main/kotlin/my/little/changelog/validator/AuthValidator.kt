package my.little.changelog.validator

import my.little.changelog.exception.ForbiddenException
import my.little.changelog.model.auth.User

object AuthValidator {

    @Throws(ForbiddenException::class)
    public fun validateAuthority(expectedUser: User, currentUser: User): ValidatorResponse {
        if (expectedUser.id.value != currentUser.id.value) {
            throw ForbiddenException()
        }
        return ValidatorResponse(emptyList())
    }
}
