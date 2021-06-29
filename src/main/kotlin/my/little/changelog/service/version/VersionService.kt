package my.little.changelog.service.version

import my.little.changelog.configuration.auth.CustomPrincipal
import my.little.changelog.model.group.Group
import my.little.changelog.model.group.GroupLatest
import my.little.changelog.model.group.dto.external.WholeGroupDto
import my.little.changelog.model.leaf.Leaf
import my.little.changelog.model.leaf.LeafLatest
import my.little.changelog.model.leaf.dto.external.WholeLeafDto
import my.little.changelog.model.leaf.dto.service.LeafDeletionDto
import my.little.changelog.model.version.dto.external.PreviousVersionsDTO
import my.little.changelog.model.version.dto.external.WholeVersion
import my.little.changelog.model.version.dto.service.ReturnedVersionDto
import my.little.changelog.model.version.dto.service.VersionCreationDto
import my.little.changelog.model.version.dto.service.VersionDeletionDto
import my.little.changelog.model.version.dto.service.toRepoDto
import my.little.changelog.model.version.toReturnedDto
import my.little.changelog.persistence.repo.*
import my.little.changelog.service.leaf.LeafService
import my.little.changelog.validator.AuthValidator
import my.little.changelog.validator.Response
import my.little.changelog.validator.Valid
import my.little.changelog.validator.VersionValidator
import org.jetbrains.exposed.dao.with
import org.jetbrains.exposed.sql.transactions.transaction

object VersionService {

    fun getVersions(projectId: Int, cp: CustomPrincipal): List<ReturnedVersionDto> = transaction {
        val project = ProjectRepo.findById(projectId)
        VersionRepo.findByProject(project).sortedBy {
            it.order
        }.map {
            it.toReturnedDto()
        }
    }

    fun createVersion(versionCreationDto: VersionCreationDto): Response<ReturnedVersionDto> = transaction {
        val project = ProjectRepo.findById(versionCreationDto.projectId)
        AuthValidator.validateAuthority(project.user, versionCreationDto.principal.user)
            .ifValidResponse {
                Valid(VersionRepo.create(versionCreationDto.toRepoDto(versionCreationDto.principal.user, project)).toReturnedDto())
            }
    }

    fun deleteVersion(deletionDto: VersionDeletionDto): Response<Unit> = transaction {
        val version = VersionRepo.findById(deletionDto.id)

        VersionValidator
            .validateLatest(version)
            .chain {
                AuthValidator.validateAuthority(version.project.user, deletionDto.principal.user)
            }
            .ifValid {
                LeafRepo.findByVersion(version).forEach {
                    LeafService.deleteLeaf(LeafDeletionDto(it.id.value, deletionDto.principal, false))
                }
                GroupRepo.findByVersion(version).forEach {
                    GroupRepo.delete(it)
                }

                VersionRepo.delete(version)
            }
    }

    fun getWholeVersion(id: Int, cp: CustomPrincipal): WholeVersion = transaction {
        val version = VersionRepo.findById(id)
        val latestVersion = VersionRepo.findLatestByProject(version.project)

        AuthValidator.validateAuthority(version.user, cp.user)

        val leaves = LeafRepo.findByVersion(version)
        val groups = GroupRepo.findGroupsAffectedByVersion(version).with(Group::version).toList()
        val earliestIds = GroupRepo.findEarliestByVids(groups.map { it.vid }).toSet()

        createDtosRecursive(
            groups.groupBy({ it.parentVid }) { it },
            leaves.groupBy({ it.groupVid }) { it },
            earliestIds,
            version.id.value,
        ).let {
            WholeVersion(version.id.value, version.id.value == latestVersion.id.value, it.first, version.name, version.project.id.value)
        }
    }

    private fun createDtosRecursive(
        groupsMap: Map<Int?, List<Group>>,
        leavesMap: Map<Int?, List<Leaf>>,
        earliestIds: Set<Int>,
        currentVersion: Int,
        value: Int? = null
    ): Pair<List<WholeGroupDto>, List<WholeLeafDto>> {
        val rootGroupDtos = groupsMap[value]?.sortedBy {
            it.order
        }?.map {
            val pair = createDtosRecursive(groupsMap, leavesMap, earliestIds, currentVersion, it.vid)
            WholeGroupDto(
                it.id.value,
                it.vid,
                it.name,
                earliestIds.contains(it.id.value),
                currentVersion == it.version.id.value,
                pair.first,
                pair.second,
                it.isDeleted
            )
        } ?: emptyList()

        val rootLeafDtos = leavesMap[value]?.sortedBy {
            it.order
        }?.map {
            WholeLeafDto(it.id.value, it.vid, it.name, it.valueType, it.value, it.isDeleted)
        } ?: emptyList()

        return rootGroupDtos to rootLeafDtos
    }

    fun getPreviousVersions(projectId: Int, cp: CustomPrincipal): PreviousVersionsDTO = transaction {
        val project = ProjectRepo.findById(projectId)
        val version = VersionRepo.findLatestByProject(project)
        val groups = GroupLatestRepo.findAllByProjectNotDeleted(project)
        val leaves = LeafLatestRepo.findAllByProjectNotDeleted(project)
        val earliestIds = GroupRepo.findEarliestByVids(groups.map { it.vid }).toSet()

        createLatestDtosRecursive(
            groups.groupBy({ it.parentVid }) { it },
            leaves.groupBy({ it.groupVid }) { it },
            earliestIds,
            version.id.value
        ).let {
            PreviousVersionsDTO(it.first)
        }
    }

    private fun createLatestDtosRecursive(
        groupsMap: Map<Int?, List<GroupLatest>>,
        leavesMap: Map<Int?, List<LeafLatest>>,
        earliestIds: Set<Int>,
        currentVersion: Int,
        value: Int? = null
    ): Pair<List<WholeGroupDto>, List<WholeLeafDto>> {
        val rootGroupDtos = groupsMap[value]?.sortedBy {
            it.order
        }?.map {
            val pair = createLatestDtosRecursive(groupsMap, leavesMap, earliestIds, currentVersion, it.vid)
            val id = it.id.value
            WholeGroupDto(
                id,
                it.vid,
                it.name,
                earliestIds.contains(id),
                currentVersion == it.version.id.value,
                pair.first,
                pair.second,
                it.isDeleted
            )
        } ?: emptyList()

        val rootLeafDtos = leavesMap[value]?.sortedBy {
            it.order
        }?.map {
            WholeLeafDto(it.id.value, it.vid, it.name, it.valueType, it.value, false)
        } ?: emptyList()

        return rootGroupDtos to rootLeafDtos
    }
}
