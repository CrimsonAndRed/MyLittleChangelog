package my.little.changelog.service

import my.little.changelog.model.group.Group
import my.little.changelog.model.group.dto.GroupDto
import my.little.changelog.model.leaf.Leaf
import my.little.changelog.model.leaf.Leaves
import my.little.changelog.model.leaf.dto.external.LeafDto
import my.little.changelog.model.version.Version
import my.little.changelog.model.version.WholeVersion
import my.little.changelog.persistence.group.GroupRepo
import my.little.changelog.persistence.group.VersionRepo
import org.jetbrains.exposed.sql.transactions.transaction

object VersionService {

    fun createVersion(): Version = transaction {
        VersionRepo.createVersion()
    }

    /**
     *
     */
    fun getWholeVersion(id: Int): WholeVersion = transaction {
        val version = VersionRepo.findVersionById(id)

        val leaves = Leaf.find { Leaves.version eq version.id.value }
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
        leavesMap: Map<Int, List<Leaf>>,
        value: Int? = null
    ): Pair<List<GroupDto>, List<LeafDto>> {
        val rootGroupDtos = groupsMap[value]?.map {
            val pair = createDtosRecursive(groupsMap, leavesMap, it.vid)
            GroupDto(it.id.value, it.vid, it.version.id.value, it.name, pair.first, pair.second)
        } ?: emptyList()

        val rootLeafDtos = leavesMap[value]?.map {
            LeafDto(it.id.value, it.vid, it.name, it.valueType, it.value)
        } ?: emptyList()

        return rootGroupDtos to rootLeafDtos
    }
}

