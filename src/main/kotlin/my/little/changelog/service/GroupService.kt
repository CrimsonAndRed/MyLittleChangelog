package my.little.changelog.service

import my.little.changelog.model.group.dto.external.ReturnedGroupDto
import my.little.changelog.model.group.dto.service.GroupCreationDto
import my.little.changelog.model.group.dto.service.GroupUpdateDto
import my.little.changelog.model.group.dto.service.toRepoDto
import my.little.changelog.model.group.toReturnedGroupDto
import my.little.changelog.persistence.group.GroupRepo
import my.little.changelog.persistence.group.VersionRepo
import org.jetbrains.exposed.sql.transactions.transaction

object GroupService {

    fun createGroup(group: GroupCreationDto): ReturnedGroupDto = transaction {
        val version = VersionRepo.findVersionById(group.versionId)
        val parentGroup = group.parentId?.let { GroupRepo.findGroupById(it) }
        GroupRepo.createGroup(group.toRepoDto(version, parentGroup))
            .toReturnedGroupDto(parentGroup?.id?.value)
    }

    fun updateGroup(groupUpdate: GroupUpdateDto): ReturnedGroupDto = transaction {
        val group = GroupRepo.findGroupById(groupUpdate.id)
        val parentGroup = groupUpdate.parentId?.let { GroupRepo.findGroupById(it) }

        group.name = groupUpdate.name
        group.parentVid = parentGroup?.vid

        GroupRepo.updateGroup(group)
            .toReturnedGroupDto(parentGroup?.id?.value)
    }
}
