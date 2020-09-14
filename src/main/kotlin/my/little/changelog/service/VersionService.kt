package my.little.changelog.service

import my.little.changelog.model.group.Group
import my.little.changelog.model.group.dto.external.WholeGroupDto
import my.little.changelog.model.leaf.Leaf
import my.little.changelog.model.leaf.dto.external.WholeLeafDto
import my.little.changelog.model.version.dto.external.WholeVersion
import my.little.changelog.model.version.dto.service.ReturnedVersionDto
import my.little.changelog.model.version.toReturnedDto
import my.little.changelog.persistence.repo.GroupRepo
import my.little.changelog.persistence.repo.LeafRepo
import my.little.changelog.persistence.repo.VersionRepo
import org.jetbrains.exposed.sql.transactions.transaction

object VersionService {

    fun createVersion(): ReturnedVersionDto = transaction {
        VersionRepo.create().toReturnedDto()
    }

    fun getWholeVersion(id: Int): WholeVersion = transaction {
        val version = VersionRepo.findById(id)

        val leaves = LeafRepo.findByVersion(version)
        val groups = GroupRepo.findGroupAffectedByVersion(version)

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
