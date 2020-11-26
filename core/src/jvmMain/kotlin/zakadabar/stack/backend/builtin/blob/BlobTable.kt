/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.builtin.blob

import org.jetbrains.exposed.dao.id.LongIdTable
import zakadabar.stack.Stack
import zakadabar.stack.backend.util.OidBlobColumnType

object BlobTable : LongIdTable("t_${Stack.shid}_blobs") {
    val name = varchar("name", 100)
    val type = varchar("type", 50)
    val size = long("size")
    val content = registerColumn<Long>("content", OidBlobColumnType())
}