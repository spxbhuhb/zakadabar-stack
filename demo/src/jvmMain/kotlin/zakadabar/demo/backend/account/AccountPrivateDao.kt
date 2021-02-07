/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.backend.account

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import zakadabar.demo.data.AccountPrivateDto
import zakadabar.stack.backend.data.builtin.principal.PrincipalDao
import zakadabar.stack.data.builtin.AccountPublicDto

class AccountPrivateDao(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<AccountPrivateDao>(AccountPrivateTable)

    var principal by PrincipalDao referencedOn AccountPrivateTable.principal

    var accountName by AccountPrivateTable.accountName
    var fullName by AccountPrivateTable.fullName
    var displayName by AccountPrivateTable.displayName
    var avatar by AccountImageDao optionalReferencedOn AccountPrivateTable.avatar

    var organizationName by AccountPrivateTable.organizationName
    var position by AccountPrivateTable.position

    var email by AccountPrivateTable.email
    var phone by AccountPrivateTable.phone

    fun toDto() = AccountPrivateDto(
        id = id.value,

        accountName = accountName,
        fullName = fullName,
        displayName = displayName,
        avatar = avatar?.id?.value,

        organizationName = organizationName,
        position = position,

        email = email,
        phone = phone
    )

    fun toPublicDto() = AccountPublicDto(
        id = id.value,

        accountName = accountName,
        fullName = fullName,
        displayName = displayName,

        organizationName = organizationName,

        email = email
    )
}