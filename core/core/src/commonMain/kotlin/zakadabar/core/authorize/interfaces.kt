/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.authorize

import zakadabar.core.data.BaseBo
import zakadabar.core.data.EntityId
import zakadabar.core.data.Secret
import zakadabar.core.util.UUID

interface RoleBlProvider {
    fun getByName(name: String): EntityId<out BaseBo>
}

interface PermissionBlProvider {
    fun getByName(name: String): EntityId<out BaseBo>
}

interface AccountBlProvider {
    fun anonymous(): AccountPublicBo
    fun readPublic(account: EntityId<out BaseBo>): AccountPublicBo
    fun authenticate(executor: Executor, accountName: String, password: Secret): AccountPublicBo

    fun anonymousV2(): AccountPublicBoV2 {
        throw NotImplementedError()
    }

    fun readPublicV2(account: EntityId<out BaseBo>): AccountPublicBoV2 {
        throw NotImplementedError()
    }

    fun authenticateV2(executor: Executor, accountName: String, password: Secret): AccountPublicBoV2 {
        throw NotImplementedError()
    }

    fun roles(accountId: EntityId<out BaseBo>): List<Pair<EntityId<out BaseBo>, String>>

    fun permissions(accountId: EntityId<out BaseBo>): List<Pair<EntityId<out BaseBo>, String>>

    /**
     * Get an [Executor] for the account with the given name.
     *
     * **Use with care, bypasses authentication!**
     */
    fun executorFor(name : String) : Executor {
        throw NotImplementedError()
    }

    /**
     * Get an [Executor] for the account with the given UUID.
     *
     * **This function DOES NOT WORK on Android!**
     *
     * I think it is because the SQLite Driver + Exposed combination does not compare
     * BLOBs/ByteArrays properly.
     *
     * **Use with care, bypasses authentication!**
     */
    fun executorFor(uuid : UUID) : Executor {
        throw NotImplementedError()
    }

}