/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.upgrade.u2021_6_to_2021_7

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.`java-time`.timestamp

@Suppress("unused")
object AccountPrivateExposedTable2021m6 : LongIdTable("account_private") {

     val validated = bool("validated")
     val locked = bool("locked")
     val expired = bool("expired")
     val credentials = varchar("credentials", 200).nullable()
     val resetKey = varchar("reset_key", 200).nullable()
     val resetKeyExpiration = timestamp("reset_key_expiration").nullable()
     val lastLoginSuccess = timestamp("last_login_success").nullable()
     val loginSuccessCount = integer("login_success_count")
     val lastLoginFail = timestamp("last_login_fail").nullable()
     val loginFailCount = integer("login_fail_count")
     val accountName = varchar("account_name", 50)
     val fullName = varchar("full_name", 100)
     val email = varchar("email", 50)
     val phone = varchar("phone", 20).nullable()
     val displayName = varchar("display_name", 50).nullable()
     val theme = varchar("theme", 50).nullable()
     val locale = varchar("locale", 20)

}