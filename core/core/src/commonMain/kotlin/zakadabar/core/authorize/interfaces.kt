/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.authorize

import zakadabar.core.data.BaseBo
import zakadabar.core.data.Secret
import zakadabar.core.data.EntityId

interface RoleBlProvider {
    fun getByName(name : String) : EntityId<out BaseBo>
}

interface AccountBlProvider {
    fun anonymous() : AccountPublicBo
    fun readPublic(account: EntityId<out BaseBo>): AccountPublicBo
    fun authenticate(executor : Executor, accountName : String, password : Secret) : AccountPublicBo
    fun roles(accountId: EntityId<out BaseBo>) : List<Pair<EntityId<out BaseBo>,String>>
}