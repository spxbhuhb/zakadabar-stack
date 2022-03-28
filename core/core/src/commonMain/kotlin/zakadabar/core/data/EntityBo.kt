/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.data

import zakadabar.core.authorize.Executor
import zakadabar.core.comm.CommConfig
import zakadabar.core.comm.EntityCommInterface

@Suppress("UNCHECKED_CAST")
interface EntityBo<T> : BaseBo {

    var id: EntityId<T>

    fun getBoNamespace(): String

    fun comm(): EntityCommInterface<T>

    suspend fun create(executor : Executor? = null, config : CommConfig? = null) = comm().create(this as T, executor, config)

    suspend fun update(executor : Executor? = null, config : CommConfig? = null) = comm().update(this as T, executor, config)

    suspend fun update(executor : Executor? = null, config : CommConfig? = null, func : T.() -> Unit) {
        (this as T).func()
        update(executor, config)
    }

    suspend fun delete(executor : Executor? = null, config : CommConfig? = null) = comm().delete(id, executor, config)

}