/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.backend.account.account

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import zakadabar.demo.data.account.AccountDto

class AccountDao(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<AccountDao>(AccountTable)

    var accountName by AccountTable.accountName
    var email by AccountTable.email
    var fullName by AccountTable.fullName
    var displayName by AccountTable.displayName
    var organizationName by AccountTable.organizationName
    var avatar by AccountImageDao optionalReferencedOn AccountTable.avatar

    fun toDto() = AccountDto(
        id = id.value,
        accountName = accountName,
        email = email,
        fullName = fullName,
        displayName = displayName,
        organizationName = organizationName,
        avatar = avatar?.id?.value
    )
}