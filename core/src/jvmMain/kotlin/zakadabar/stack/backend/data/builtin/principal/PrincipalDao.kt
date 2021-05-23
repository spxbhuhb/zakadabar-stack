/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.data.builtin.principal

import kotlinx.datetime.toKotlinInstant
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import zakadabar.stack.backend.data.entityId
import zakadabar.stack.data.builtin.account.PrincipalBo

class PrincipalDao(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<PrincipalDao>(PrincipalTable)

    var validated by PrincipalTable.validated
    var locked by PrincipalTable.locked
    var expired by PrincipalTable.expired

    var credentials by PrincipalTable.credentials

    var resetKey by PrincipalTable.resetKey
    var resetExpiration by PrincipalTable.resetExpiration

    var lastLoginSuccess by PrincipalTable.lastLoginSuccess
    var loginSuccessCount by PrincipalTable.loginSuccessCount

    var lastLoginFail by PrincipalTable.lastLoginFail
    var loginFailCount by PrincipalTable.loginFailCount

    fun toBo() = PrincipalBo(
        id = id.entityId(),

        validated = validated,
        locked = locked,
        expired = expired,

        lastLoginSuccess = lastLoginSuccess?.toKotlinInstant(),
        loginSuccessCount = loginSuccessCount,

        lastLoginFail = lastLoginFail?.toKotlinInstant(),
        loginFailCount = loginFailCount
    )

}