/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.upgrade.u2021_6_to_2021_7

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.`java-time`.timestamp

@Suppress("unused")
object AccountCredentialsExposedTable2021m7 : Table("account_credential") {

    val entityId = reference("account", AccountPrivateExposedTable2021m7)
    val type = varchar("type", 50)
    val value = text("value")
    val expiration = timestamp("expiration").nullable()

    override val primaryKey = PrimaryKey(entityId, type)

}