package my.little.changelog.service.diff

import my.little.changelog.model.diff.dto.service.DifferenceDto
import my.little.changelog.model.diff.dto.service.ReturnedDifferenceDto
import my.little.changelog.model.group.Group
import my.little.changelog.model.group.dto.service.GroupDifferenceDto
import my.little.changelog.model.leaf.Leaf
import my.little.changelog.model.leaf.dto.service.LeafDifferenceDto
import my.little.changelog.persistence.repo.GroupRepo
import my.little.changelog.persistence.repo.LeafRepo
import my.little.changelog.persistence.repo.VersionRepo
import org.jetbrains.exposed.sql.transactions.transaction

object DifferenceService {

    fun findDifference(dto: DifferenceDto): ReturnedDifferenceDto = transaction {
        val toVersion = VersionRepo.findById(dto.toVersion)
        val fromVersion = VersionRepo.findById(dto.fromVersion)
        val leavesTo = LeafRepo.findDifferentialLeaves(fromVersion, toVersion)
        val groupsTo = GroupRepo.findGroupsAffectedByLeaves(leavesTo, toVersion)

        val leavesFrom = LeafRepo.findPreDifferentialLeaves(fromVersion, leavesTo)

        createDtosRecursive(
            groupsTo.groupBy { it.parentVid },
            leavesTo.groupBy { it.groupVid },
            leavesFrom.map { it.groupVid to it }.toMap(),
        ).let {
            ReturnedDifferenceDto(dto.fromVersion, dto.toVersion, it.first, it.second)
        }
    }

    private fun createDtosRecursive(
        groupsToMap: Map<Int?, List<Group>>,
        leavesToMap: Map<Int?, List<Leaf>>,
        leavesFromMap: Map<Int?, Leaf>,
        value: Int? = null
    ): Pair<List<GroupDifferenceDto>, List<Pair<LeafDifferenceDto?, LeafDifferenceDto>>> {
        val rootGroupDtos = groupsToMap[value]?.map {
            val pair = createDtosRecursive(groupsToMap, leavesToMap, leavesFromMap, it.vid)
            GroupDifferenceDto(it.id.value, it.vid, it.name, pair.first, pair.second)
        } ?: emptyList()

        val rootLeafDtos = leavesToMap[value]?.map {
            val leafFrom = leavesFromMap[value]

            Pair(
                leafFrom?.let { fl -> LeafDifferenceDto(fl.id.value, fl.vid, fl.name, fl.valueType, fl.value) },
                LeafDifferenceDto(it.id.value, it.vid, it.name, it.valueType, it.value)
            )
        } ?: emptyList()

        return rootGroupDtos to rootLeafDtos
    }
}
