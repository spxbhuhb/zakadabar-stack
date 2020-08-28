/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.builtin.account.data

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import zakadabar.stack.data.security.CommonAccountDto

class AccountDao(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<AccountDao>(AccountTable)

    var emailAddress by AccountTable.emailAddress
    var fullName by AccountTable.fullName
    var displayName by AccountTable.displayName
    var organizationName by AccountTable.organizationName
    var avatar by AccountTable.avatar

    fun toDto() = CommonAccountDto(
        id = id.value,
        entityDto = null, // do not include entity by default
        emailAddress = emailAddress,
        fullName = fullName,
        displayName = displayName,
        organizationName = organizationName,
        avatar = avatar?.value
    )
}