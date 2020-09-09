package my.little.changelog.service

import my.little.changelog.model.group.dto.external.ReturnedGroupDto
import my.little.changelog.model.group.dto.service.GroupCreationDto
import my.little.changelog.model.group.dto.service.GroupUpdateDto
import my.little.changelog.model.group.dto.service.toRepoDto
import my.little.changelog.model.group.toReturnedGroupDto
import my.little.changelog.persistence.repo.GroupRepo
import my.little.changelog.persistence.repo.VersionRepo
import org.jetbrains.exposed.sql.transactions.transaction

object GroupService {

    fun createGroup(group: GroupCreationDto): ReturnedGroupDto = transaction {
        val version = VersionRepo.findById(group.versionId)
        val parentGroup = group.parentId?.let { GroupRepo.findById(it) }
        GroupRepo.create(group.toRepoDto(version, parentGroup))
            .toReturnedGroupDto(parentGroup?.id?.value)
    }

    fun updateGroup(groupUpdate: GroupUpdateDto): ReturnedGroupDto = transaction {
        val group = GroupRepo.findById(groupUpdate.id)
        val parentGroup = groupUpdate.parentId?.let { GroupRepo.findById(it) }

        group.apply {
            name = groupUpdate.name
            parentVid = parentGroup?.vid
        }

        GroupRepo.update(group)
            .toReturnedGroupDto(parentGroup?.id?.value)
    }
}
