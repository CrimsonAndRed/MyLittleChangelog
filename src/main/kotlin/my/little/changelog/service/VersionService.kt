package my.little.changelog.service

import my.little.changelog.model.group.Group
import my.little.changelog.model.group.Groups
import my.little.changelog.model.group.dto.GroupDto
import my.little.changelog.model.leaf.Leaf
import my.little.changelog.model.leaf.Leaves
import my.little.changelog.model.leaf.dto.LeafDto
import my.little.changelog.model.version.Version
import my.little.changelog.model.version.WholeVersion
import my.little.changelog.model.version.dto.VersionDto
import my.little.changelog.model.version.toDto
import org.jetbrains.exposed.sql.or
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
fun getWholeVersion(id: Int): WholeVersion = transaction {
    val version = Version[id]
    val leaves = Leaf.find { Leaves.version eq version.id.value }
    val groupVids = leaves.map {
        it.groupVid
    }
    val groups = Group.find {
        // TODO неверный вариант выбора групп
        (Groups.version eq version.id.value) or (Groups.vid inList groupVids)
    }
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

        GroupDto(
            id = it.id.value,
            vid = it.vid,
            version = it.version.id.value,
            name = it.name,
            groupContent = pair.first,
            leafContent = pair.second,
        )
    } ?: emptyList()

    val rootLeafDtos = leavesMap[value]?.map {
        LeafDto(
            id = it.id.value,
            vid = it.vid,
            name = it.name,
            valueType = it.valueType,
            value = it.value,
        )
    } ?: emptyList()

    return Pair(
        rootGroupDtos,
        rootLeafDtos
    )
}

