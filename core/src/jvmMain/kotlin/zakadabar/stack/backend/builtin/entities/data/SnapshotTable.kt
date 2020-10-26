/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.builtin.entities.data

import org.jetbrains.exposed.dao.id.LongIdTable
import zakadabar.stack.Stack
import zakadabar.stack.backend.util.OidBlobColumnType

object SnapshotTable : LongIdTable("t_${Stack.shid}_snapshots") {
    val entity = reference("entity", EntityTable).index()
    val revision = long("revision")
    val size = long("size")
    val content = registerColumn<Long>("content", OidBlobColumnType())
}