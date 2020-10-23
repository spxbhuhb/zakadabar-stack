/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package zakadabar.stack.backend.builtin.session

import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import zakadabar.stack.backend.builtin.session.data.SessionTable
import zakadabar.stack.backend.data.DtoBackend
import zakadabar.stack.data.builtin.session.SessionDto
import zakadabar.stack.util.PublicApi

@PublicApi
object SessionBackend : DtoBackend<SessionDto>() {

    override val dtoClass = SessionDto::class

    override fun init() {
        transaction {
            SchemaUtils.createMissingTablesAndColumns(
                SessionTable
            )
        }
    }

}