package my.little.changelog.validator

import my.little.changelog.model.group.Group
import my.little.changelog.model.group.dto.service.GroupCreationDto
import my.little.changelog.model.group.dto.service.GroupUpdateDto
import my.little.changelog.persistence.repo.GroupRepo

object GroupValidator {

    fun validateNew(group: GroupCreationDto): ValidatorResponse {
        val errors: MutableList<String> = mutableListOf()
        this.validateName(group.name, errors)

        return ValidatorResponse(errors)
    }

    fun validateUpdate(group: GroupUpdateDto, currentGroup: Group): ValidatorResponse {
        val errors: MutableList<String> = mutableListOf()
        this.validateName(group.name, errors)
        if (group.parentVid != currentGroup.parentVid) {
            val movedToChild = GroupRepo.findParents(group.parentVid)
                .map { it.vid }
                .contains(currentGroup.parentVid)
            if (movedToChild) {
                errors.add("Can not move group to child")
            }
        }

        return ValidatorResponse(errors)
    }

    private fun validateName(name: String, errors: MutableList<String>) {
        if (name.isBlank()) {
            errors.add(ValidatorMessage.isBlank("name"))
        }
    }
}
