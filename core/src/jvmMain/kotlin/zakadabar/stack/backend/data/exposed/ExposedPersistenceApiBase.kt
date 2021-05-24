/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.data.exposed

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.transactions.TransactionManager
import zakadabar.stack.backend.data.Sql

interface ExposedPersistenceApi {

    fun exposedCommit() {
        TransactionManager.current().commit()
    }

    fun exposedRollback() {
        TransactionManager.current().rollback()
    }

    /**
     * This function registers an SQL table. Call from [onModuleLoad].
     * After all modules are created the [Server] will call createMissingTablesAndColumns
     */
    operator fun Table.unaryPlus() {
        Sql.tables += this
    }
}