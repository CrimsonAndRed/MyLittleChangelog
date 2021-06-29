package my.little.changelog.service.group

import my.little.changelog.configuration.auth.CustomPrincipal
import my.little.changelog.model.group.dto.external.GroupDeletionDto
import my.little.changelog.model.group.dto.service.GroupCreationDto
import my.little.changelog.model.group.dto.service.GroupUpdateDto
import my.little.changelog.model.group.dto.service.ReturnedGroupDto
import my.little.changelog.model.group.dto.service.toRepoDto
import my.little.changelog.model.group.toReturnedDto
import my.little.changelog.model.leaf.dto.repo.LeafCreationDto
import my.little.changelog.persistence.repo.*
import my.little.changelog.validator.*
import org.jetbrains.exposed.sql.transactions.transaction

object GroupService {

    fun createGroup(group: GroupCreationDto): Response<ReturnedGroupDto> = transaction {
        val version = VersionRepo.findById(group.versionId)
        AuthValidator.validateAuthority(group.principal.user, version.user)
            .chain {
                VersionValidator.validateLatest(version)
            }
            .chain {
                GroupValidator.validateNew(group)
            }
            .ifValid {
                GroupRepo.create(group.toRepoDto(version)).toReturnedDto()
            }
    }

    fun updateGroup(groupUpdate: GroupUpdateDto): Response<Unit> = transaction {
        val group = GroupRepo.findById(groupUpdate.id)
        AuthValidator.validateAuthority(groupUpdate.principal.user, group.version.user)
            .chain {
                VersionValidator.validateLatest(group.version)
            }
            .chain {
                GroupValidator.validateUpdate(groupUpdate, group)
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

    fun deleteGroup(groupDelete: GroupDeletionDto, dropHierarchy: Boolean, dropCompletely: Boolean): Response<Unit> = transaction {
        val group = GroupRepo.findById(groupDelete.id)
        val currentVersion = group.version
        val versionId = currentVersion.id.value
        AuthValidator.validateAuthority(groupDelete.principal.user, currentVersion.user)
            .chain {
                VersionValidator.validateLatest(currentVersion)
            }
            .chain {
                if (!dropHierarchy) {
                    GroupRepo.findSublatestGroup(group.vid, versionId)
                        ?: return@chain ValidatorResponse(listOf("Can not delete group without hierarchy in version it was created"))
                }
                ValidatorResponse(emptyList())
            }
            .ifValid {
                when {
                    dropCompletely -> {
                        val latestGroupsHierarchy = GroupLatestRepo.findHierarchyToChildByVid(group.vid)
                        val latestGroupVids = latestGroupsHierarchy.map { it.vid }
                        val latestLeaves = LeafLatestRepo.findAllByGroupsNotDeleted(latestGroupVids)
                        val leaves = LeafRepo.findByIds(latestLeaves.map { it.id.value })
                        val groups = GroupRepo.findByIds(latestGroupsHierarchy.map { it.id.value })

                        val groupsByVids = groups.map { it.vid to it }.toMap()

                        groups.forEach {
                            if (!it.isDeleted) {
                                if (it.version.id.value == versionId) {
                                    it.isDeleted = true
                                    GroupRepo.update(it)
                                } else {
                                    GroupRepo.create(
                                        my.little.changelog.model.group.dto.repo.GroupCreationDto(
                                            it.name,
                                            it.vid,
                                            it.parentVid,
                                            currentVersion,
                                            it.order,
                                            true
                                        )
                                    )
                                }
                            }
                        }

                        leaves.forEach {
                            if (it.version.id.value == versionId) {
                                it.isDeleted = true
                                LeafRepo.update(it)
                            } else {
                                LeafRepo.create(
                                    LeafCreationDto(
                                        it.vid,
                                        it.name,
                                        it.valueType,
                                        it.value,
                                        groupsByVids[it.groupVid]!!,
                                        currentVersion,
                                        true
                                    )
                                )
                            }
                        }
                    }
                    dropHierarchy -> {
                        val latestGroupsHierarchy = GroupLatestRepo.findHierarchyToChildByVid(group.vid)

                        val leaves = LeafRepo.findCurrentGroupsLeaves(
                            latestGroupsHierarchy.map { it.vid },
                            currentVersion
                        )
                        leaves.forEach {
                            it.delete()
                        }
                        val groups = GroupRepo.findByVids(
                            latestGroupsHierarchy.filter { it.version.id.value == versionId }.map { it.vid },
                            currentVersion
                        )

                        groups.forEach { g ->
                            GroupRepo.delete(g)
                        }
                    }
                    else -> {
                        GroupRepo.delete(group)
                    }
                }
            }
    }

    fun changePosition(groupId: Int, changeAgainstId: Int, cp: CustomPrincipal): Response<Unit> = transaction {
        val group = GroupRepo.findById(groupId)
        val groupChangeAgainst = GroupRepo.findById(changeAgainstId)
        val latestVersion = VersionRepo.findLatestByProject(group.version.project)
        val validator = AuthValidator.validateAuthority(cp.user, group.version.user)
            .chain {
                AuthValidator.validateAuthority(cp.user, groupChangeAgainst.version.user)
            }
            .chain {
                VersionValidator.validateLatest(group.version)
            }
            .chain {
                ValidatorResponse
                    .ofSimple("Could not modify groups that is not in the same parent group. IDs [${group.id.value}] [${groupChangeAgainst.id.value}]") {
                        group.parentVid != groupChangeAgainst.parentVid
                    }
            }

        if (groupChangeAgainst.version.id != latestVersion.id) {
            validator.chain {
                createGroup(
                    GroupCreationDto(
                        groupChangeAgainst.name,
                        groupChangeAgainst.vid,
                        groupChangeAgainst.parentVid,
                        latestVersion.id.value,
                        group.order,
                        cp
                    )
                ).mapValidation()
                    .chain {
                        group.apply { order = groupChangeAgainst.order }
                        GroupRepo.update(group)
                        ValidatorResponse(emptyList())
                    }
            }.toResponse(Unit)
        } else {
            validator
                .chain {
                    val tmpOrder = group.order
                    group.apply { order = groupChangeAgainst.order }
                    groupChangeAgainst.apply { order = tmpOrder }
                    GroupRepo.update(group)
                    GroupRepo.update(groupChangeAgainst)
                    ValidatorResponse(emptyList())
                }
                .toResponse(Unit)
        }
    }
}
