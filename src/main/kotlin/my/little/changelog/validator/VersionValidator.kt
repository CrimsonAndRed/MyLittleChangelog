package my.little.changelog.validator

import my.little.changelog.model.auth.User
import my.little.changelog.model.version.Version
import my.little.changelog.persistence.repo.VersionRepo

object VersionValidator {

    fun validateLatest(version: Version, user: User): ValidatorResponse {
        val errors = mutableListOf<String>()
        if (version.id != VersionRepo.findLatestByProject(version.project).id) {
            errors.add("Version is not latest!")
        }
        return ValidatorResponse(errors)
    }
}
