/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
@file:Suppress("UNUSED_PARAMETER", "unused")

package zakadabar.demo.backend.account.account

import io.ktor.routing.*
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import zakadabar.demo.data.account.AccountPublicDto
import zakadabar.stack.backend.data.record.RecordBackend
import zakadabar.stack.util.Executor

object AccountPublicBackend : RecordBackend<AccountPublicDto>() {

    override val dtoClass = AccountPublicDto::class

    override fun install(route: Route) {
        route.crud()
    }

    override fun all(executor: Executor) = transaction {
        AccountPrivateTable
            .selectAll()
            .map(AccountPrivateTable::toPublicDto)
    }

    override fun read(executor: Executor, recordId: Long) = transaction {
        AccountPrivateDao[recordId].toPublicDto()
    }

}