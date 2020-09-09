package my.little.changelog.service

import my.little.changelog.model.leaf.Leaf
import my.little.changelog.model.leaf.dto.service.LeafCreationDto
import my.little.changelog.model.leaf.dto.service.LeafUpdateDto
import my.little.changelog.model.leaf.dto.service.toRepoDto
import my.little.changelog.persistence.group.GroupRepo
import my.little.changelog.persistence.group.LeafRepo
import my.little.changelog.persistence.group.VersionRepo
import org.jetbrains.exposed.sql.transactions.transaction

object LeafService {

    fun createLeaf(leaf: LeafCreationDto): Leaf = transaction {
        val version = VersionRepo.findVersionById(leaf.versionId)
        val group = GroupRepo.findGroupById(leaf.groupId)
        LeafRepo.createLeaf(leaf.toRepoDto(version, group))
    }

    fun updateLeaf(leafUpdate: LeafUpdateDto): Leaf = transaction {
        val leaf = LeafRepo.findLeafById(leafUpdate.id)
        val parentGroup = leafUpdate.parentId?.let { GroupRepo.findGroupById(it) }

        leaf.apply {
            name = leafUpdate.name
            value = leafUpdate.value
            valueType = leafUpdate.valueType
            groupVid = parentGroup?.vid
        }

        LeafRepo.updateLeaf(leaf)
    }
}
