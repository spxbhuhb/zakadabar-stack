/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.data.entity

import kotlinx.serialization.KSerializer

abstract class EntityBoCompanion<T : EntityBo<T>>(
    val boNamespace: String
) {

    private var _comm: EntityCommInterface<T>? = null

    private fun makeComm(): EntityCommInterface<T> {
        val nc = makeEntityComm(this)
        _comm = nc
        return nc
    }

    var comm: EntityCommInterface<T>
        get() = _comm ?: makeComm()
        set(value) {
            _comm = value
        }

    abstract fun serializer(): KSerializer<T>

    suspend fun read(id: EntityId<T>) = comm.read(id)

    suspend fun all() = comm.all()

    suspend fun allAsMap() = comm.all().associateBy { it.id }

}
