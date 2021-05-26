/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.exposed

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.transactions.transaction
import zakadabar.stack.data.builtin.settings.DatabaseSettingsBo
import zakadabar.stack.data.entity.EntityId

inline operator fun <reified T : LongEntity> LongEntityClass<T>.get(entityId: EntityId<*>) = this[entityId.toLong()]

inline fun <reified T> EntityID<Long>.entityId() = EntityId<T>(this.value)

object Sql {

    lateinit var dataSource: HikariDataSource

    val tables = mutableListOf<Table>()

    fun onCreate(config: DatabaseSettingsBo) {
        val hikariConfig = HikariConfig()

        hikariConfig.driverClassName = config.driverClassName
        hikariConfig.jdbcUrl = config.jdbcUrl
        hikariConfig.username = config.username
        hikariConfig.password = config.password
        hikariConfig.maximumPoolSize = 10
        hikariConfig.isAutoCommit = false
        hikariConfig.transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        hikariConfig.validate()

        dataSource = HikariDataSource(hikariConfig)

        Database.connect(dataSource)
    }

    fun onStart() {
        transaction {
            SchemaUtils.createMissingTablesAndColumns(*tables.toTypedArray())
        }
    }
}