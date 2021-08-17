/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.accounts.server.ktor

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.`java-time`.timestamp

/**
 * Stores [StackSession] objects.
 */
object SessionTable : Table("session") {

    val id = varchar("id", 50)
    val lastAccess = timestamp("last_access")
    val content = blob("content")

    override val primaryKey = PrimaryKey(id)
}