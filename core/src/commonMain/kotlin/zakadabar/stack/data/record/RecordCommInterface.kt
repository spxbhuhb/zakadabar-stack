/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.data.record

import io.ktor.http.*
import kotlinx.serialization.KSerializer
import zakadabar.stack.data.action.ActionCommInterface
import zakadabar.stack.data.builtin.BlobDto
import zakadabar.stack.data.query.QueryCommInterface
import zakadabar.stack.data.util.AuthenticationException
import zakadabar.stack.data.util.AuthorizationException
import zakadabar.stack.data.util.CommunicationException

/**
 * Interface to be implemented on the client side for communication. An
 * actual implementation is zakadabar.stack.data.record.RecordComm.
 */
interface RecordCommInterface<T> {

    /**
     * Creates a new record on the server.
     *
     * @param  dto  DTO of the record to create.
     *
     * @return The record DTO of the created record.
     *
     * @throws IllegalArgumentException the dto is invalid (HTTP status code 400)
     * @throws AuthenticationException the principal is not authenticated (HTTP status code 401)
     * @throws AuthorizationException the principal is authenticated but not authorized to create objects of this type (HTTP status code 403)
     * @throws NotImplementedError this function is not implemented on the server side (HTTP status code 501)
     * @throws RuntimeException if there is a general server side processing error (HTTP status code 5xx)
     * @throws CommunicationException server unavailable, request timeout, any not 400,401,403 error codes
     */
    suspend fun create(dto: T): T

    /**
     * Fetches a record.
     *
     * @param  id  Id of the record.
     *
     * @return  DTO of the fetched record.
     *
     * @throws NoSuchElementException if the record with the given id does not exists (HTTP status code 404)
     * @throws AuthenticationException the principal is not authenticated (HTTP status code 401)
     * @throws AuthorizationException the principal is authenticated but not authorized to create objects of this type (HTTP status code 403)
     * @throws NotImplementedError this function is not implemented on the server side (HTTP status code 501)
     * @throws RuntimeException if there is a general server side processing error (HTTP status code 5xx)
     * @throws CommunicationException server unavailable, request timeout, any not 400,401,403,404 error codes
     */
    suspend fun read(id: Long): T

    /**
     * Updates a record on the server.
     *
     * @param  dto  DTO of the record to update.
     *
     * @return The updated DTO.
     *
     * @throws NoSuchElementException if the record with the given id does not exists (HTTP status code 404)
     * @throws IllegalArgumentException the dto is invalid (HTTP status code 400)
     * @throws AuthenticationException the principal is not authenticated (HTTP status code 401)
     * @throws AuthorizationException the principal is authenticated but not authorized to create objects of this type (HTTP status code 403)
     * @throws NotImplementedError this function is not implemented on the server side (HTTP status code 501)
     * @throws RuntimeException if there is a general server side processing error (HTTP status code 5xx)
     * @throws CommunicationException server unavailable, request timeout, any not 400,401,403 error codes
     */
    suspend fun update(dto: T): T

    /**
     * Deletes a record on the server.
     *
     * @param  id  Id of the record to delete.
     *
     * @throws NoSuchElementException if the record with the given id does not exists (HTTP status code 404)
     * @throws AuthenticationException the principal is not authenticated (HTTP status code 401)
     * @throws AuthorizationException the principal is authenticated but not authorized to create objects of this type (HTTP status code 403)
     * @throws NotImplementedError this function is not implemented on the server side (HTTP status code 501)
     * @throws RuntimeException if there is a general server side processing error (HTTP status code 5xx)
     * @throws CommunicationException server unavailable, request timeout, any not 400,401,403 error codes
     */
    suspend fun delete(id: Long)

    /**
     * Retrieves all records of the given type.
     *
     * @return  List of record DTOs.
     *
     * @throws AuthenticationException the principal is not authenticated (HTTP status code 401)
     * @throws AuthorizationException the principal is authenticated but not authorized to create objects of this type (HTTP status code 403)
     * @throws NotImplementedError this function is not implemented on the server side (HTTP status code 501)
     * @throws RuntimeException if there is a general server side processing error (HTTP status code 5xx)
     * @throws CommunicationException server unavailable, request timeout, any not 400,401,403 error codes
     */
    suspend fun all(): List<T>

    /**
     * Create a BLOB that belongs to the given record.
     *
     * Works only when the backend supports BLOBs for the record type.
     *
     * Calls [callback] once before the upload starts and then whenever
     * the state of the upload changes.
     *
     * Blob ID is 0 until the upload finishes. The actual id of the blob
     * is set when callback is called with state [BlobCreateState.Done].
     *
     * @param  dataRecordId  Id of the record the new BLOB belongs to.
     * @param  name      Name of the BLOB, typically the file name.
     * @param  type      Type of the BLOB, typically the MIME type.
     * @param  data      BLOB data, a Javascript [Blob].
     * @param  callback  Callback function to report progress, completion or error.
     *
     * @return A DTO which contains data of the blob. The `id` is 0 in this DTO.
     *
     */
    fun blobCreate(
        dataRecordId: Long?, name: String, type: String, data: Any,
        callback: (dto: BlobDto, state: BlobCreateState, uploaded: Long) -> Unit
    )

    /**
     * Create a BLOB that belongs to the given record.
     *
     * Works only when the backend supports BLOBs for the record type.
     *
     * @param  dataRecordId  Id of the record the new BLOB belongs to.
     * @param  name      Name of the BLOB, typically the file name.
     * @param  type      Type of the BLOB, typically the MIME type.
     * @param  data      BLOB data, a Javascript [Blob].
     *
     * @return A DTO which contains data of the blob.
     *
     * @throws IllegalArgumentException the request dto is invalid (HTTP status code 400)
     * @throws NoSuchElementException if the data record with the given id does not exists (HTTP status code 404)
     * @throws AuthenticationException the principal is not authenticated (HTTP status code 401)
     * @throws AuthorizationException the principal is authenticated but not authorized to create objects of this type (HTTP status code 403)
     * @throws NotImplementedError this function is not implemented on the server side (HTTP status code 501)
     * @throws RuntimeException if there is a general server side processing error (HTTP status code 5xx)
     * @throws CommunicationException server unavailable, request timeout, any not 400,401,403, 404 error codes
     */
    suspend fun blobCreate(dataRecordId: Long?, name: String, type: ContentType, data: ByteArray): BlobDto

    /**
     * Retrieves metadata of BLOBs.
     *
     * @param  dataRecordId  Id of the record the BLOBs belong to.
     *
     * @return  List of BLOB metadata.
     *
     * @throws NoSuchElementException if the data record with the given id does not exists (HTTP status code 404)
     * @throws AuthenticationException the principal is not authenticated (HTTP status code 401)
     * @throws AuthorizationException the principal is authenticated but not authorized to create objects of this type (HTTP status code 403)
     * @throws NotImplementedError this function is not implemented on the server side (HTTP status code 501)
     * @throws RuntimeException if there is a general server side processing error (HTTP status code 5xx)
     * @throws CommunicationException server unavailable, request timeout, any not 400,401,403, 404 error codes
     */
    suspend fun blobMetaRead(dataRecordId: Long): List<BlobDto>

    /**
     * Updates an metadata of a blob: recordId, name, type fields.
     *
     * @return The updated DTO.
     *
     * @throws NoSuchElementException if the blob with the given id does not exists (HTTP status code 404)
     * @throws IllegalArgumentException the dto is invalid (HTTP status code 400)
     * @throws AuthenticationException the principal is not authenticated (HTTP status code 401)
     * @throws AuthorizationException the principal is authenticated but not authorized to create objects of this type (HTTP status code 403)
     * @throws NotImplementedError this function is not implemented on the server side (HTTP status code 501)
     * @throws RuntimeException if there is a general server side processing error (HTTP status code 5xx)
     * @throws CommunicationException server unavailable, request timeout, any not 400,401,403 error codes
     */
    suspend fun blobMetaUpdate(dto: BlobDto): BlobDto

    /**
     * Deletes a BLOB.
     *
     * @param  dataRecordId  Id of the data record the blob to delete belongs to.
     * @param  blobId        Id of the blob to delete.
     *
     * @throws NoSuchElementException if the record or the blob with the given id does not exists (HTTP status code 404)
     * @throws IllegalArgumentException the dto is invalid (HTTP status code 400)
     * @throws AuthenticationException the principal is not authenticated (HTTP status code 401)
     * @throws AuthorizationException the principal is authenticated but not authorized to create objects of this type (HTTP status code 403)
     * @throws NotImplementedError this function is not implemented on the server side (HTTP status code 501)
     * @throws RuntimeException if there is a general server side processing error (HTTP status code 5xx)
     * @throws CommunicationException server unavailable, request timeout, any not 400,401,403 error codes
     */
    suspend fun blobDelete(dataRecordId: Long, blobId: Long)
}