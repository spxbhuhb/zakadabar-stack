/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.data.record

import kotlinx.serialization.KSerializer

abstract class RecordDtoCompanion<T : RecordDto<T>>() {

    constructor(builder: RecordDtoCompanion<T>.() -> Unit) : this() {
        this.builder()
    }

    lateinit var namespace: String

    private var _comm: RecordCommInterface<T>? = null

    private fun makeComm(): RecordCommInterface<T> {
        val nc = makeRecordComm(this)
        _comm = nc
        return nc
    }

    var comm: RecordCommInterface<T>
        get() = _comm ?: makeComm()
        set(value) {
            _comm = value
        }

    abstract fun serializer(): KSerializer<T>

    suspend fun read(id: RecordId<T>) = comm.read(id)

    suspend fun all() = comm.all()

    suspend fun allAsMap() = comm.all().associateBy { it.id }

}
