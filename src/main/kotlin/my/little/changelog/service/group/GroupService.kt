package my.little.changelog.service.group

import my.little.changelog.model.exception.VersionIsNotLatestException
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
import org.jetbrains.exposed.sql.transactions.transaction

object GroupService {

    fun createGroup(group: GroupCreationDto): ReturnedGroupDto = transaction {
        val version = VersionRepo.findById(group.versionId)
        if (version.id != VersionRepo.findLatest().id) {
            throw VersionIsNotLatestException()
        }
        GroupRepo.create(group.toRepoDto(version))
            .toReturnedDto()
    }

    fun updateGroup(groupUpdate: GroupUpdateDto): ReturnedGroupDto = transaction {
        val group = GroupRepo.findById(groupUpdate.id)

        if (group.version.id != VersionRepo.findLatest().id) {
            throw VersionIsNotLatestException()
        }

        group.apply {
            name = groupUpdate.name
            parentVid = groupUpdate.parentVid
        }

        GroupRepo.update(group)
            .toReturnedDto()
    }

    fun deleteGroup(groupDelete: GroupDeletionDto, dropHierarchy: Boolean): ReturnedGroupDto? = transaction {
        val group = GroupRepo.findById(groupDelete.id)
        val versionId = group.version.id.value
        if (versionId != VersionRepo.findLatest().id.value) {
            throw VersionIsNotLatestException()
        }

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
                throw RuntimeException("Can not delete group without hierarchy in version it was created")
            }
            GroupRepo.delete(group)
        }

        sublatestGroup?.let {
            val parentGroupId = sublatestGroup.parentVid?.let {
                GroupLatestRepo.findByVid(it).id.value
            }
            ReturnedGroupDto(sublatestGroup.id.value, sublatestGroup.vid, sublatestGroup.name, parentGroupId)
        }
    }
}
