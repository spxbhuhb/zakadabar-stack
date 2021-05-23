/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.data.builtin.account

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import zakadabar.stack.backend.data.builtin.principal.PrincipalDao
import zakadabar.stack.backend.data.entityId
import zakadabar.stack.data.builtin.account.AccountPrivateBo
import zakadabar.stack.data.builtin.account.AccountPublicBo

class AccountPrivateDao(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<AccountPrivateDao>(AccountPrivateTable)

    var principal by PrincipalDao referencedOn AccountPrivateTable.principal

    var accountName by AccountPrivateTable.accountName
    var fullName by AccountPrivateTable.fullName
    var email by AccountPrivateTable.email

    var displayName by AccountPrivateTable.displayName
    var theme by AccountPrivateTable.theme
    var locale by AccountPrivateTable.locale
    var avatar by AccountImageDao optionalReferencedOn AccountPrivateTable.avatar

    var organizationName by AccountPrivateTable.organizationName
    var position by AccountPrivateTable.position
    var phone by AccountPrivateTable.phone

    fun toBo() = AccountPrivateBo(
        id = id.entityId(),

        principal = principal.id.entityId(),

        accountName = accountName,
        fullName = fullName,
        email = email,

        displayName = displayName,
        theme = theme,
        locale = locale,
        avatar = avatar?.id?.value,

        organizationName = organizationName,
        position = position,
        phone = phone
    )

    fun toPublicBo(addEmail: Boolean) = AccountPublicBo(
        id = id.entityId(),

        accountName = accountName,
        fullName = fullName,
        email = if (addEmail) email else null,

        displayName = displayName,
        theme = theme,
        locale = locale,

        organizationName = organizationName
    )
}