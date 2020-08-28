/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.builtin.security

import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import zakadabar.stack.backend.builtin.security.data.AclEntryTable
import zakadabar.stack.backend.builtin.security.data.RoleGrantTable
import zakadabar.stack.backend.extend.BackendModule
import zakadabar.stack.util.UUID

object Module : BackendModule() {

    override val uuid = UUID("00dfb09c-0a85-4f11-a8cf-b06b5f1d78dd")

    override fun init() = transaction {
        SchemaUtils.createMissingTablesAndColumns(
            AclEntryTable,
            RoleGrantTable
        )
    }

}