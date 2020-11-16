package my.little.changelog.service.group

import my.little.changelog.model.group.dto.external.GroupDeletionDto
import my.little.changelog.model.group.dto.service.GroupCreationDto
import my.little.changelog.model.group.dto.service.GroupUpdateDto
import my.little.changelog.model.group.dto.service.ReturnedGroupDto
import my.little.changelog.model.group.dto.service.toRepoDto
import my.little.changelog.model.group.toReturnedDto
import my.little.changelog.persistence.repo.GroupLatestRepo
import my.little.changelog.persistence.repo.GroupRepo
import my.little.changelog.persistence.repo.LeafRepo
import my.little.changelog.persistence.repo.VersionRepo
import my.little.changelog.validator.Err
import my.little.changelog.validator.Response
import my.little.changelog.validator.Valid
import my.little.changelog.validator.VersionValidator
import org.jetbrains.exposed.sql.transactions.transaction

object GroupService {

    fun createGroup(group: GroupCreationDto): Response<ReturnedGroupDto> = transaction {
        val version = VersionRepo.findById(group.versionId)
        val validatorResponse = VersionValidator.validateLatest(version)
        if (validatorResponse.isValid()) {

            val returned = GroupRepo.create(group.toRepoDto(version))
                .toReturnedDto()
            return@transaction Valid(returned)
        } else {
            return@transaction Err(validatorResponse.errors)
        }
    }

    fun updateGroup(groupUpdate: GroupUpdateDto): Response<ReturnedGroupDto> = transaction {
        val group = GroupRepo.findById(groupUpdate.id)
        val validatorResponse = VersionValidator.validateLatest(group.version)
        if (validatorResponse.isValid()) {

            group.apply {
                name = groupUpdate.name
                parentVid = groupUpdate.parentVid
            }

            val returned = GroupRepo.update(group)
                .toReturnedDto()
            return@transaction Valid(returned)
        } else {
            return@transaction Err(validatorResponse.errors)
        }
    }

    fun deleteGroup(groupDelete: GroupDeletionDto, dropHierarchy: Boolean): Response<ReturnedGroupDto?> = transaction {
        val group = GroupRepo.findById(groupDelete.id)
        val versionId = group.version.id.value
        val validatorResponse = VersionValidator.validateLatest(group.version)
        if (validatorResponse.isValid()) {
            val sublatestGroup = GroupRepo.findSublatestGroup(group.vid, versionId)
            if (dropHierarchy) {
                val latestGroupsHierarchy = GroupLatestRepo.findHierarchyToChildByVid(group.vid)

                val leaves = LeafRepo.findCurrentGroupsLeaves(latestGroupsHierarchy.map { it.vid }, group.version)
                leaves.forEach {
                    it.delete()
                }
                val groups = GroupRepo.findByVids(
                    latestGroupsHierarchy.filter { it.version.id.value == versionId }.map { it.vid },
                    group.version
                )

                groups.forEach { g ->
                    GroupRepo.delete(g)
                }
            } else {
                if (sublatestGroup == null) {
                    return@transaction Err(listOf("Can not delete group without hierarchy in version it was created"))
                }
                GroupRepo.delete(group)
            }

            val returned = sublatestGroup?.let {
                val parentGroupId = sublatestGroup.parentVid?.let {
                    GroupLatestRepo.findByVid(it).id.value
                }
                ReturnedGroupDto(sublatestGroup.id.value, sublatestGroup.vid, sublatestGroup.name, parentGroupId)
            }
            return@transaction Valid(returned)
        } else {
            return@transaction Err(validatorResponse.errors)
        }
    }
}
