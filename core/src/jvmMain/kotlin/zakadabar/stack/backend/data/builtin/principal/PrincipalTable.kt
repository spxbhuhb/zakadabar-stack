/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.data.builtin.principal

import kotlinx.datetime.toKotlinInstant
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.`java-time`.timestamp
import zakadabar.stack.data.builtin.PrincipalDto

object PrincipalTable : LongIdTable("principals") {

    val validated = bool("validated").default(false)
    val locked = bool("locked").default(false)
    val expired = bool("expired").default(false)

    val credentials = varchar("credentials", length = 255).nullable()

    val resetKey = varchar("reset_key", length = 32).nullable()
    val resetExpiration = timestamp("reset_expiration").nullable()

    val lastLoginSuccess = timestamp("last_login_success").nullable()
    val loginSuccessCount = integer("login_success_count").default(0)

    val lastLoginFail = timestamp("last_login_fail").nullable()
    val loginFailCount = integer("login_fail_count").default(0)

    fun toDto(row: ResultRow) = PrincipalDto(
        id = row[id].value,

        validated = row[validated],
        locked = row[locked],
        expired = row[expired],

        lastLoginSuccess = row[lastLoginSuccess]?.toKotlinInstant(),
        loginSuccessCount = row[loginSuccessCount],

        lastLoginFail = row[lastLoginFail]?.toKotlinInstant(),
        loginFailCount = row[loginFailCount]
    )

}