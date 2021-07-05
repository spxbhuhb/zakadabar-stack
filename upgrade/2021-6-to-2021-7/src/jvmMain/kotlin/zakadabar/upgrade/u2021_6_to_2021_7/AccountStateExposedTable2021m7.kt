/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.upgrade.u2021_6_to_2021_7

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.`java-time`.timestamp

@Suppress("unused")
object AccountStateExposedTable2021m7 : Table("account_state") {

    val entityId = reference("account", AccountPrivateExposedTable2021m7)
    val validated = bool("validated")
    val locked = bool("locked")
    val expired = bool("expired")
    val anonymized = bool("anonymized")
    val lastLoginSuccess = timestamp("last_login_success").nullable()
    val loginSuccessCount = integer("login_success_count")
    val lastLoginFail = timestamp("last_login_fail").nullable()
    val loginFailCount = integer("login_fail_count")

    override val primaryKey = PrimaryKey(entityId)

}