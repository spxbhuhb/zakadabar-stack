/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.data.builtin.account

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class AccountImageDao(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<AccountImageDao>(AccountImageTable)

    // we don't use the fields directly, so no need to add them
}