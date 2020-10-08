package my.little.changelog.service.version

import my.little.changelog.model.exception.VersionIsNotLatestException
import my.little.changelog.model.group.Group
import my.little.changelog.model.group.dto.external.GroupDeletionDto
import my.little.changelog.model.group.dto.external.WholeGroupDto
import my.little.changelog.model.leaf.Leaf
import my.little.changelog.model.leaf.dto.external.WholeLeafDto
import my.little.changelog.model.leaf.dto.service.LeafDeletionDto
import my.little.changelog.model.version.dto.external.WholeVersion
import my.little.changelog.model.version.dto.service.ReturnedVersionDto
import my.little.changelog.model.version.dto.service.VersionDeletionDto
import my.little.changelog.model.version.toReturnedDto
import my.little.changelog.persistence.repo.GroupRepo
import my.little.changelog.persistence.repo.LeafRepo
import my.little.changelog.persistence.repo.VersionRepo
import my.little.changelog.service.group.GroupService
import my.little.changelog.service.leaf.LeafService
import org.jetbrains.exposed.sql.transactions.transaction

object VersionService {

    fun getVersions(): List<ReturnedVersionDto> = transaction {
        VersionRepo.findAll().map {
            it.toReturnedDto()
        }
    }

    fun createVersion(): ReturnedVersionDto = transaction {
        VersionRepo.create().toReturnedDto()
    }

    fun deleteVersion(deletionDto: VersionDeletionDto) = transaction {
        val version = VersionRepo.findById(deletionDto.id)
        if (version.id != VersionRepo.findLatest().id) {
            throw VersionIsNotLatestException()
        }
        LeafRepo.findByVersion(version).forEach {
            LeafService.deleteLeaf(LeafDeletionDto(it.id.value))
        }
        GroupRepo.findByVersion(version).forEach {
            GroupService.deleteGroup(GroupDeletionDto(it.id.value))
        }
        VersionRepo.delete(version)
    }

    fun getWholeVersion(id: Int): WholeVersion = transaction {
        val version = VersionRepo.findById(id)

        val leaves = LeafRepo.findByVersion(version)
        val groups = GroupRepo.findGroupsAffectedByVersion(version)

        createDtosRecursive(
            groups.groupBy({ it.parentVid }) { it },
            leaves.groupBy({ it.groupVid }) { it }
        ).let {
            WholeVersion(version.id.value, it.first, it.second)
        }
    }

    private fun createDtosRecursive(
        groupsMap: Map<Int?, List<Group>>,
        leavesMap: Map<Int?, List<Leaf>>,
        value: Int? = null
    ): Pair<List<WholeGroupDto>, List<WholeLeafDto>> {
        val rootGroupDtos = groupsMap[value]?.map {
            val pair = createDtosRecursive(groupsMap, leavesMap, it.vid)
            WholeGroupDto(it.id.value, it.vid, it.name, pair.first, pair.second)
        } ?: emptyList()

        val rootLeafDtos = leavesMap[value]?.map {
            WholeLeafDto(it.id.value, it.vid, it.name, it.valueType, it.value)
        } ?: emptyList()

        return rootGroupDtos to rootLeafDtos
    }
}
