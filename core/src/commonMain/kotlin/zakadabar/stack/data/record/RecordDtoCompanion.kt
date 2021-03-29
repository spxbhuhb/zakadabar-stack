/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.data.record

import kotlinx.serialization.KSerializer
import zakadabar.stack.data.action.ActionDtoCompanion
import zakadabar.stack.data.query.QueryDtoCompanion

abstract class RecordDtoCompanion<T : RecordDto<T>>() {

    constructor(builder: RecordDtoCompanion<T>.() -> Unit) : this() {
        this.builder()
    }

    lateinit var recordType: String

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

    suspend fun read(id: Long) = comm.read(id)

    suspend fun all() = comm.all()

    suspend fun allAsMap() = comm.all().associateBy { it.id }

}
