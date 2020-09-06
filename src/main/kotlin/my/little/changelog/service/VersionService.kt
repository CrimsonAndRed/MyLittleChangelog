package my.little.changelog.service

import my.little.changelog.model.group.Group
import my.little.changelog.model.group.dto.GroupDto
import my.little.changelog.model.leaf.Leaf
import my.little.changelog.model.leaf.Leaves
import my.little.changelog.model.leaf.dto.LeafDto
import my.little.changelog.model.version.Version
import my.little.changelog.model.version.WholeVersion
import my.little.changelog.model.version.dto.VersionDto
import my.little.changelog.model.version.toDto
import my.little.changelog.persistence.group.GroupsRepo
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

fun createVersion(): VersionDto {
    return transaction {
        Version.new {
        }
    }.toDto()
}

/**
 *
 */
suspend fun getWholeVersion(id: Int): WholeVersion = newSuspendedTransaction {
    val version = Version[id]

    val leaves = Leaf.find { Leaves.version eq version.id.value }
    val groups = GroupsRepo.findGroupAffectedByVersion(version)

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

