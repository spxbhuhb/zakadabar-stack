/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.authorize

import zakadabar.stack.data.BaseBo
import zakadabar.stack.data.builtin.account.AccountPublicBo
import zakadabar.stack.data.builtin.misc.Secret
import zakadabar.stack.data.entity.EntityId

class InvalidCredentials : Exception()
class AccountNotValidatedException : Exception()
class AccountLockedException : Exception()
class AccountExpiredException : Exception()

interface RoleBlProvider {
    fun getByName(name : String) : EntityId<out BaseBo>
}

interface AccountBlProvider {
    fun anonymous() : AccountPublicBo
    fun readPublic(account: EntityId<out BaseBo>): AccountPublicBo
    fun authenticate(accountName : String, password : Secret) : AccountPublicBo
    fun roles(accountId: EntityId<out BaseBo>) : List<Pair<EntityId<out BaseBo>,String>>
}