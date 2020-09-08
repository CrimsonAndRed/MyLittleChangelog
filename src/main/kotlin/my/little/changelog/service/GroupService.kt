package my.little.changelog.service

import my.little.changelog.model.group.dto.external.NewGroupDto
import my.little.changelog.model.group.dto.service.GroupCreationDto
import my.little.changelog.model.group.dto.service.toRepoDto
import my.little.changelog.model.group.toNewGroupDto
import my.little.changelog.persistence.group.GroupRepo
import my.little.changelog.persistence.group.VersionRepo
import org.jetbrains.exposed.sql.transactions.transaction

object GroupService {

    fun createGroup(group: GroupCreationDto): NewGroupDto = transaction {
        val version = VersionRepo.findVersionById(group.versionId)
        val parentGroup = group.parentId?.let { GroupRepo.findGroupById(it) }
        GroupRepo.createGroup(group.toRepoDto(version, parentGroup))
            .toNewGroupDto(parentGroup?.id?.value)
    }
}
