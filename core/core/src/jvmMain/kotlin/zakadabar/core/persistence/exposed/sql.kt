
/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.persistence.exposed

import ch.qos.logback.classic.Level
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.jdbc.JdbcConnectionImpl
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory
import zakadabar.core.data.EntityId
import zakadabar.core.persistence.sql.SqlProvider
import zakadabar.core.server.DatabaseSettingsBo
import java.sql.Connection

inline operator fun <reified T : LongEntity> LongEntityClass<T>.get(entityId: EntityId<*>) = this[entityId.toLong()]

inline fun <reified T> EntityID<Long>.entityId() = EntityId<T>(this.value)

object Sql : SqlProvider {

    lateinit var dataSource: HikariDataSource

    override val tables = mutableListOf<Table>()

    override fun onCreate(config: DatabaseSettingsBo) {
        val hikariConfig = HikariConfig()

        hikariConfig.driverClassName = config.driverClassName
        hikariConfig.jdbcUrl = config.jdbcUrl
        hikariConfig.username = config.username
        hikariConfig.password = config.password
        hikariConfig.maximumPoolSize = 10
        hikariConfig.isAutoCommit = false
        hikariConfig.transactionIsolation = config.isolationLevel
        hikariConfig.validate()

        dataSource = HikariDataSource(hikariConfig)

        if (config.debugSql) {
            val logger : ch.qos.logback.classic.Logger = LoggerFactory.getLogger("Exposed") as ch.qos.logback.classic.Logger
            logger.level = Level.DEBUG
        }

        TransactionManager.defaultDatabase = Database.connect(dataSource)

        when (config.isolationLevel) {
            "TRANSACTION_NONE" -> Connection.TRANSACTION_NONE
            "TRANSACTION_READ_COMMITTED" -> Connection.TRANSACTION_READ_COMMITTED
            "TRANSACTION_READ_UNCOMMITTED" -> Connection.TRANSACTION_READ_UNCOMMITTED
            "TRANSACTION_REPEATABLE_READ" -> Connection.TRANSACTION_REPEATABLE_READ
            "TRANSACTION_SERIALIZABLE" -> Connection.TRANSACTION_SERIALIZABLE
            else -> null
        }?.let {
            TransactionManager.manager.defaultIsolationLevel = it
        }
    }

    override fun onStart(noDbSchemaUpdate : Boolean) {
        if (noDbSchemaUpdate) return
        transaction {
            SchemaUtils.createMissingTablesAndColumns(*tables.toTypedArray())
        }
    }

    /**
     * Drop the given column from the table if it exists. As of now, Exposed does
     * not support "IF EXIST".
     */
    fun Transaction.dropColumnIfExists(table : Table, column : String) {
        if (this.connection is JdbcConnectionImpl) {
            this.connection.metadata {
                val columns = columns(table)[table] ?: emptyList()
                if (columns.find { it.name.lowercase() == "node_address" } != null) {
                    Column<String>(table, "node_address", TextColumnType())
                        .dropStatement()
                        .forEach { statement ->
                            exec(statement)
                        }
                }
            }
        }
    }
}