/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package zakadabar.lib.accounts.data

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import zakadabar.stack.data.BaseBo
import zakadabar.stack.data.entity.EntityId
import zakadabar.stack.data.schema.BoSchema

/**
 * State of an account.
 *
 * @property  accountId         The id of the [AccountPrivateBo] BO this credentials belong to.
 * @property  validated         True when this account is validated y email, by
 *                              other means, or when validation is switched off.
 *                              Prevents login when false.
 * @property  locked            True when the account is locked and not allowed to
 *                              log in. Set manually be security officers. Prevents
 *                              login when true.
 * @property  expired           True when the account is expired. Prevents login when true.
 * @property  anonymized        True when this account is anonymized. Prevents log in when true.
 * @property  lastLoginSuccess  Time of the last successful login on this account.
 * @property  loginSuccessCount Number of successful logins on this account (since created).
 * @property  lastLoginFail     Time of last login fail.
 * @property  loginFailCount    Number of failed logins on this account since the last
 *                              successful login.
*/
@Serializable
open class AccountStateBo(

    var accountId: EntityId<AccountPrivateBo>,

    var validated : Boolean,
    var locked : Boolean,
    var expired : Boolean,
    var anonymized : Boolean,
    var lastLoginSuccess : Instant?,
    var loginSuccessCount : Int,
    var lastLoginFail : Instant?,
    var loginFailCount : Int,

) : BaseBo {

    override fun schema() = BoSchema {
        + ::accountId

        + ::validated
        + ::locked
        + ::expired
        + ::anonymized
        + ::lastLoginSuccess
        + ::loginSuccessCount
        + ::lastLoginFail
        + ::loginFailCount
    }
}