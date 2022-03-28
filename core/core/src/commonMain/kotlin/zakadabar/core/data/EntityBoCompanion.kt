/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.data

import kotlinx.serialization.KSerializer
import zakadabar.core.authorize.Executor
import zakadabar.core.comm.CommConfig
import zakadabar.core.comm.EntityCommInterface
import zakadabar.core.comm.makeEntityComm

abstract class EntityBoCompanion<T : EntityBo<T>>(
    val boNamespace: String,
    val commConfig : CommConfig? = null
) {

    private var _comm: EntityCommInterface<T>? = null

    private fun makeComm(): EntityCommInterface<T> {
        val nc = makeEntityComm(this,commConfig)
        _comm = nc
        return nc
    }

    var comm: EntityCommInterface<T>
        get() = _comm ?: makeComm()
        set(value) {
            _comm = value
        }

    abstract fun serializer(): KSerializer<T>

    suspend fun read(id: EntityId<T>, executor : Executor? = null, config : CommConfig? = null) = comm.read(id, executor, config)

    suspend fun delete(id: EntityId<T>, executor : Executor? = null, config : CommConfig? = null) = comm.delete(id, executor, config)

    suspend fun all(executor : Executor? = null, config : CommConfig? = null) = comm.all(executor, config)

    suspend fun allAsMap(executor : Executor? = null, config : CommConfig? = null) = comm.all(executor, config).associateBy { it.id }
}