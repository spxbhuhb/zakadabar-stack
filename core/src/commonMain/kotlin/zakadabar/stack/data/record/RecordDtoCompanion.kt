/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.data.record

import kotlinx.serialization.KSerializer
import zakadabar.stack.comm.http.Comm
import zakadabar.stack.comm.http.makeComm
import zakadabar.stack.data.query.QueryDtoCompanion

abstract class RecordDtoCompanion<T : RecordDto<T>>() {

    constructor(builder: RecordDtoCompanion<T>.() -> Unit) : this() {
        this.builder()
    }

    lateinit var recordType: String

    private var _comm: Comm<T>? = null

    private fun makeComm(): Comm<T> {
        val nc = makeComm(this)
        _comm = nc
        return nc
    }

    var comm: Comm<T>
        get() = _comm ?: makeComm()
        set(value) {
            _comm = value
        }

    open val queries = lazy {
        mutableMapOf<String, QueryDtoCompanion<*, *>>()
    }

    abstract fun serializer(): KSerializer<T>

    suspend fun read(id: Long) = comm.read(id)

    suspend fun all() = comm.all()

    suspend fun allAsMap() = comm.all().associateBy { it.id }

    operator fun QueryDtoCompanion<T, *>.unaryPlus() {
        val name = this::class.simpleName ?: return
        queries.value[name] = this
        base = this@RecordDtoCompanion
    }
}
