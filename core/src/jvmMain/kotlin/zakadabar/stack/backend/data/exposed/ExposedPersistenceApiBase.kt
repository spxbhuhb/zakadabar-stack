/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.data.entity

import org.jetbrains.exposed.sql.Table
import zakadabar.stack.backend.Server
import zakadabar.stack.backend.data.Sql

interface ExposedPersistenceApi {

    /**
     * This function registers an SQL table. Call from [onModuleLoad].
     * After all modules are created the [Server] will call createMissingTablesAndColumns
     */
    operator fun Table.unaryPlus() {
        Sql.tables += this
    }

}