/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.builtin.account

import io.ktor.routing.*
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import zakadabar.stack.backend.builtin.account.data.AccountTable
import zakadabar.stack.backend.extend.BackendModule
import zakadabar.stack.backend.extend.entityRestApi
import zakadabar.stack.data.security.CommonAccountDto
import zakadabar.stack.util.UUID

object Module : BackendModule() {

    override val uuid = UUID("ba18f3dd-e916-4e9a-9474-2491004573d2")

    override fun init() {

        transaction {
            SchemaUtils.createMissingTablesAndColumns(
                AccountTable
            )
        }

    }

    override fun install(route: Route) {
        entityRestApi(route, Backend, CommonAccountDto::class, CommonAccountDto.type)
    }

}