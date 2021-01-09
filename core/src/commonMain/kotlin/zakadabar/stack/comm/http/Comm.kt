/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.comm.http

import kotlinx.serialization.KSerializer
import zakadabar.stack.data.builtin.BlobDto

/**
 * Interface to be implemented on the client side for communication. An
 * actual implementation is zakadabar.stack.frontend.comm.http.RecordComm.
 */
interface Comm<T> {

    suspend fun create(dto: T): T

    suspend fun read(id: Long): T

    suspend fun update(dto: T): T

    suspend fun delete(id: Long)

    suspend fun all(): List<T>

    suspend fun <RQ : Any> query(request: RQ, requestSerializer: KSerializer<RQ>): List<T>

    suspend fun <RQ : Any, RS> query(request: RQ, requestSerializer: KSerializer<RQ>, responseSerializer: KSerializer<List<RS>>): List<RS>

    fun blobCreate(
        dataRecordId: Long?, name: String, type: String, data: Any,
        callback: (dto: BlobDto, state: BlobCreateState, uploaded: Long) -> Unit
    )

    suspend fun blobMetaRead(dataRecordId: Long): List<BlobDto>

    suspend fun blobMetaUpdate(dto: BlobDto): BlobDto

    suspend fun blobDelete(dataRecordId: Long, blobId: Long)
}