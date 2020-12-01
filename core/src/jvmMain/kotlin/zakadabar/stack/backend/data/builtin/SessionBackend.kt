/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package zakadabar.stack.backend.data.builtin

import zakadabar.stack.backend.data.RecordBackend
import zakadabar.stack.data.builtin.SessionDto
import zakadabar.stack.util.PublicApi

@PublicApi
object SessionBackend : RecordBackend<SessionDto>() {

    override val dtoClass = SessionDto::class

    override fun init() {
//        transaction {
//            SchemaUtils.createMissingTablesAndColumns(
//                SessionTable
//            )
//        }
    }

}