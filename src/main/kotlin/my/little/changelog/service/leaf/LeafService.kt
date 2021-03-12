package my.little.changelog.service.leaf

import my.little.changelog.configuration.auth.CustomPrincipal
import my.little.changelog.model.leaf.dto.service.LeafCreationDto
import my.little.changelog.model.leaf.dto.service.LeafDeletionDto
import my.little.changelog.model.leaf.dto.service.LeafReturnedDto
import my.little.changelog.model.leaf.dto.service.LeafUpdateDto
import my.little.changelog.model.leaf.dto.service.toRepoDto
import my.little.changelog.model.leaf.toReturnedDto
import my.little.changelog.persistence.repo.GroupRepo
import my.little.changelog.persistence.repo.LeafRepo
import my.little.changelog.persistence.repo.VersionRepo
import my.little.changelog.validator.LeafValidator
import my.little.changelog.validator.Response
import my.little.changelog.validator.ValidatorResponse
import my.little.changelog.validator.VersionValidator
import org.jetbrains.exposed.sql.transactions.transaction

object LeafService {

    fun createLeaf(leaf: LeafCreationDto): Response<LeafReturnedDto> = transaction {
        val version = VersionRepo.findById(leaf.versionId)

        VersionValidator.validateLatest(version, leaf.principal.user)
            .chain {
                LeafValidator.validateNew(leaf)
            }
            .ifValid {
                val group = GroupRepo.findById(leaf.groupId)
                LeafRepo.create(leaf.toRepoDto(version, group)).toReturnedDto()
            }
    }

    fun updateLeaf(leafUpdate: LeafUpdateDto): Response<Unit> = transaction {
        val leaf = LeafRepo.findById(leafUpdate.id)
        val newParentGroup = GroupRepo.findLatestGroupByVid(leafUpdate.parentVid)
        VersionValidator.validateLatest(leaf.version, leafUpdate.principal.user)
            .chain {
                LeafValidator.validateUpdate(leafUpdate)
            }
            .ifValid {
                leaf.apply {
                    name = leafUpdate.name
                    value = leafUpdate.value
                    valueType = leafUpdate.valueType
                    groupVid = newParentGroup.vid
                }
                LeafRepo.update(leaf)
            }
            .mapEmpty()
    }

    fun deleteLeaf(leafDeletionDto: LeafDeletionDto): Response<Unit> = transaction {
        val leaf = LeafRepo.findById(leafDeletionDto.id)

        VersionValidator.validateLatest(leaf.version, leafDeletionDto.principal.user)
            .ifValid {
                if (leafDeletionDto.completely) {
                    leaf.isDeleted = true
                    LeafRepo.update(leaf)
                } else {
                    LeafRepo.delete(leaf)
                }
                Unit
            }
    }

    fun changePosition(leafId: Int, changeAgainstId: Int, cp: CustomPrincipal): Response<Unit> = transaction {
        val leaf = LeafRepo.findById(leafId)
        val leafChangeAgainst = LeafRepo.findById(changeAgainstId)
        VersionValidator.validateLatest(leaf.version, cp.user)
            .chain { VersionValidator.validateLatest(leafChangeAgainst.version, cp.user) }
            .chain {
                ValidatorResponse
                    .ofSimple("Could not modify leaves that is not in the same group. IDs [${leaf.id.value}] [${leafChangeAgainst.id.value}]") {
                        leaf.groupVid != leafChangeAgainst.groupVid
                    }
            }
            .ifValid {
                val tmpOrder = leaf.order
                leaf.apply { order = leafChangeAgainst.order }
                leafChangeAgainst.apply { order = tmpOrder }
                LeafRepo.update(leaf)
                LeafRepo.update(leafChangeAgainst)
            }
            .mapEmpty()
    }
}
