package my.little.changelog.persistence.repo

import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.statements.api.PreparedStatementApi
import org.slf4j.LoggerFactory
import java.sql.ResultSet

fun Transaction.raw(query: String, columns: Array<String>, f: PreparedStatementApi.() -> Unit): ResultSet {
    return connection.prepareStatement(query, columns)
        .apply {
            connection.prepareStatement(query, columns)
            f()
            val logger = LoggerFactory.getLogger(Transaction::class.java)
            logger.debug("Execute raw query:\n$query")
        }
        .executeQuery()
}
