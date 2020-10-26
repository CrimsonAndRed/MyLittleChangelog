package my.little.changelog.persistence.repo

import my.little.changelog.model.group.GroupLatest
import my.little.changelog.model.group.GroupsLatest
import my.little.changelog.persistence.AbstractCrudRepository
import org.jetbrains.exposed.sql.transactions.transaction

object GroupLatestRepo : AbstractCrudRepository<GroupLatest, Int>(GroupLatest) {

    fun findByVid(vid: Int): GroupLatest = transaction {
        GroupLatest.find { GroupsLatest.vid eq vid }.single()
    }
}
