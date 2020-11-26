/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.data.record

import zakadabar.stack.comm.http.Comm
import zakadabar.stack.data.schema.DtoSchema

@Suppress("UNCHECKED_CAST")
interface RecordDto<T> {

    val id: RecordId<T>

    fun getRecordType(): String

    fun comm(): Comm<T>

    fun schema() = DtoSchema.NO_VALIDATION

    suspend fun create() = comm().create(this as T)

    suspend fun update() = comm().update(this as T)

    suspend fun delete() = comm().delete(id)

}