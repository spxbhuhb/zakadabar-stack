/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.upgrade.u2021_6_to_2021_7

import org.jetbrains.exposed.dao.id.LongIdTable

@Suppress("unused")
object AccountPrivateExposedTable2021m7 : LongIdTable("account_private") {

    val accountName = varchar("account_name", 50)
    val fullName = varchar("full_name", 100)
    val email = varchar("email", 50)
    val phone = varchar("phone", 20).nullable()
    val theme = varchar("theme", 50).nullable()
    val locale = varchar("locale", 20)

}