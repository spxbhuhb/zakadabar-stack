/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.data.record

import kotlinx.serialization.KSerializer
import zakadabar.stack.comm.http.Comm

abstract class RecordDtoCompanion<T : RecordDto<T>> {

    abstract val type: String

    lateinit var comm: Comm<T>

    abstract fun serializer(): KSerializer<T>

    suspend fun read(id: Long) = comm.read(id)

}
