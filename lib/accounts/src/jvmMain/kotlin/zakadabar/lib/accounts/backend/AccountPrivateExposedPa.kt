/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.accounts.backend

import org.jetbrains.exposed.sql.select
import zakadabar.lib.accounts.data.AccountPrivateBo
import zakadabar.stack.data.builtin.misc.Secret
import zakadabar.stack.data.entity.EntityId

class AccountPrivateExposedPa : AccountPrivateExposedPaGen() {

    fun readByName(name: String) = table.select { table.accountName eq name }.first().toBo()

    internal fun readCredentials(id: EntityId<AccountPrivateBo>) =
        table
            .slice(table.credentials)
            .select { table.id eq id.toLong() }
            .map { Secret(it[table.credentials] ?: throw NoSuchElementException() ) }
            .first()
}