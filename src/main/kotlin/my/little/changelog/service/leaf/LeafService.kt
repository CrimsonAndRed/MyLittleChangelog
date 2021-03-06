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
import my.little.changelog.validator.*
import org.jetbrains.exposed.sql.transactions.transaction

object LeafService {

    fun createLeaf(leaf: LeafCreationDto): Response<LeafReturnedDto> = transaction {
        val version = VersionRepo.findById(leaf.versionId)

        VersionValidator.validateLatest(version)
            .chain {
                LeafValidator.validateNew(leaf)
            }
            .chain {
                AuthValidator.validateAuthority(version.project.user, leaf.principal.user)
            }
            .ifValid {
                val group = GroupRepo.findById(leaf.groupId)
                LeafRepo.create(leaf.toRepoDto(version, group)).toReturnedDto()
            }
    }

    fun updateLeaf(leafUpdate: LeafUpdateDto): Response<Unit> = transaction {
        val leaf = LeafRepo.findById(leafUpdate.id)
        val newParentGroup = GroupRepo.findLatestGroupByVid(leafUpdate.parentVid)
        VersionValidator.validateLatest(leaf.version)
            .chain {
                LeafValidator.validateUpdate(leafUpdate)
            }
            .chain {
                AuthValidator.validateAuthority(leaf.version.project.user, leafUpdate.principal.user)
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

        VersionValidator.validateLatest(leaf.version)
            .chain {
                AuthValidator.validateAuthority(leaf.version.project.user, leafDeletionDto.principal.user)
            }
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
        VersionValidator.validateLatest(leaf.version)
            .chain { VersionValidator.validateLatest(leafChangeAgainst.version) }
            .chain {
                ValidatorResponse
                    .ofSimple("Could not modify leaves that is not in the same group. IDs [${leaf.id.value}] [${leafChangeAgainst.id.value}]") {
                        leaf.groupVid != leafChangeAgainst.groupVid
                    }
            }
            .chain {
                AuthValidator.validateAuthority(leaf.version.user, cp.user)
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
