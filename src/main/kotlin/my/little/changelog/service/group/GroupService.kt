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
import my.little.changelog.validator.GroupValidator
import my.little.changelog.validator.Response
import my.little.changelog.validator.ValidatorResponse
import my.little.changelog.validator.VersionValidator
import org.jetbrains.exposed.sql.transactions.transaction

object GroupService {

    fun createGroup(group: GroupCreationDto): Response<ReturnedGroupDto> = transaction {
        val version = VersionRepo.findById(group.versionId)
        VersionValidator.validateLatest(version)
            .chain {
                GroupValidator.validateNew(group)
            }
            .ifValid {
                GroupRepo.create(group.toRepoDto(version)).toReturnedDto()
            }
    }

    fun updateGroup(groupUpdate: GroupUpdateDto): Response<Unit> = transaction {
        val group = GroupRepo.findById(groupUpdate.id)
        VersionValidator.validateLatest(group.version)
            .chain {
                GroupValidator.validateUpdate(groupUpdate)
            }
            .ifValid {
                group.apply {
                    name = groupUpdate.name
                    parentVid = groupUpdate.parentVid
                }

                GroupRepo.update(group)
                Unit
            }
    }

    fun deleteGroup(groupDelete: GroupDeletionDto, dropHierarchy: Boolean): Response<Unit> = transaction {
        val group = GroupRepo.findById(groupDelete.id)
        val versionId = group.version.id.value
        VersionValidator.validateLatest(group.version)
            .chain {
                if (!dropHierarchy) {
                    GroupRepo.findSublatestGroup(group.vid, versionId)
                        ?: return@chain ValidatorResponse(listOf("Can not delete group without hierarchy in version it was created"))
                }
                ValidatorResponse(emptyList())
            }
            .ifValid {
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
                    GroupRepo.delete(group)
                }
            }
    }
}
