package my.little.changelog.service

import my.little.changelog.model.leaf.Leaf
import my.little.changelog.model.leaf.dto.service.LeafCreationDto
import my.little.changelog.model.leaf.dto.service.LeafUpdateDto
import my.little.changelog.model.leaf.dto.service.toRepoDto
import my.little.changelog.persistence.repo.GroupRepo
import my.little.changelog.persistence.repo.LeafRepo
import my.little.changelog.persistence.repo.VersionRepo
import org.jetbrains.exposed.sql.transactions.transaction

object LeafService {

    fun createLeaf(leaf: LeafCreationDto): Leaf = transaction {
        val version = VersionRepo.findById(leaf.versionId)
        val group = GroupRepo.findById(leaf.groupId)
        LeafRepo.create(leaf.toRepoDto(version, group))
    }

    fun updateLeaf(leafUpdate: LeafUpdateDto): Leaf = transaction {
        val leaf = LeafRepo.findById(leafUpdate.id)
        val parentGroup = leafUpdate.parentId?.let { GroupRepo.findById(it) }

        leaf.apply {
            name = leafUpdate.name
            value = leafUpdate.value
            valueType = leafUpdate.valueType
            groupVid = parentGroup?.vid
        }

        LeafRepo.update(leaf)
    }
}
