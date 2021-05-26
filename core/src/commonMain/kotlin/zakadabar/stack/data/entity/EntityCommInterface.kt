/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.data.entity

import zakadabar.stack.data.DataConflictException
import zakadabar.stack.data.builtin.BlobBo

/**
 * Interface to be implemented on the client side for communication.
 */
interface EntityCommInterface<T> {

    /**
     * Creates a new entity on the server.
     *
     * @param  bo  BO of the entity to create.
     *
     * @return The BO of the created entity.
     *
     * @throws IllegalArgumentException the bo is invalid (HTTP status code 400)
     * @throws DataConflictException the server reported a data conflict (HTTP status code 409)
     * @throws NotImplementedError this function is not implemented on the server side (HTTP status code 501)
     * @throws RuntimeException if there is a general server side processing error (HTTP status code 4xx, 5xx)
     */
    suspend fun create(bo: T): T

    /**
     * Fetches an entity.
     *
     * @param  id  Id of the entity.
     *
     * @return  BO of the fetched entity.
     *
     * @throws IllegalArgumentException the bo is invalid (HTTP status code 400)
     * @throws NoSuchElementException if the record with the given id does not exists (HTTP status code 404)
     * @throws DataConflictException the server reported a data conflict (HTTP status code 409)
     * @throws NotImplementedError this function is not implemented on the server side (HTTP status code 501)
     * @throws RuntimeException if there is a general server side processing error (HTTP status code 4xx, 5xx)
     */
    suspend fun read(id: EntityId<T>): T

    /**
     * Fetches an entity.
     *
     * @param  id  Id of the entity.
     *
     * @return  BO of the fetched entity.
     *
     * @throws IllegalArgumentException the bo is invalid (HTTP status code 400)
     * @throws NoSuchElementException if the record with the given id does not exists (HTTP status code 404)
     * @throws DataConflictException the server reported a data conflict (HTTP status code 409)
     * @throws NotImplementedError this function is not implemented on the server side (HTTP status code 501)
     * @throws RuntimeException if there is a general server side processing error (HTTP status code 4xx, 5xx)
     */
    suspend fun read(id : Long) = read(EntityId(id))

    /**
     * Fetches an entity.
     *
     * @param  id  Id of the entity.
     *
     * @return  BO of the fetched entity.
     *
     * @throws IllegalArgumentException the bo is invalid (HTTP status code 400)
     * @throws NoSuchElementException if the record with the given id does not exists (HTTP status code 404)
     * @throws DataConflictException the server reported a data conflict (HTTP status code 409)
     * @throws NotImplementedError this function is not implemented on the server side (HTTP status code 501)
     * @throws RuntimeException if there is a general server side processing error (HTTP status code 4xx, 5xx)
     */
    suspend fun read(id : String) = read(EntityId(id))

    /**
     * Updates an entity on the server.
     *
     * @param  bo  BO of the entity to update.
     *
     * @return The updated BO.
     *
     * @throws IllegalArgumentException the bo is invalid (HTTP status code 400)
     * @throws NoSuchElementException if the record with the given id does not exists (HTTP status code 404)
     * @throws DataConflictException the server reported a data conflict (HTTP status code 409)
     * @throws NotImplementedError this function is not implemented on the server side (HTTP status code 501)
     * @throws RuntimeException if there is a general server side processing error (HTTP status code 4xx, 5xx)
     */
    suspend fun update(bo: T): T

    /**
     * Deletes an entity on the server.
     *
     * @param  id  Id of the entity to delete.
     *
     * @throws IllegalArgumentException the bo is invalid (HTTP status code 400)
     * @throws NoSuchElementException if the record with the given id does not exists (HTTP status code 404)
     * @throws DataConflictException the server reported a data conflict (HTTP status code 409)
     * @throws NotImplementedError this function is not implemented on the server side (HTTP status code 501)
     * @throws RuntimeException if there is a general server side processing error (HTTP status code 4xx, 5xx)
     */
    suspend fun delete(id: EntityId<T>)

    /**
     * Retrieves BOs of all entities of the given type.
     *
     * @return  List of entity BOs.
     *
     * @throws IllegalArgumentException the bo is invalid (HTTP status code 400)
     * @throws NoSuchElementException if the record with the given id does not exists (HTTP status code 404)
     * @throws DataConflictException the server reported a data conflict (HTTP status code 409)
     * @throws NotImplementedError this function is not implemented on the server side (HTTP status code 501)
     * @throws RuntimeException if there is a general server side processing error (HTTP status code 4xx, 5xx)
     */
    suspend fun all(): List<T>

    /**
     * Create a BLOB that belongs to the given entity.
     *
     * Works only when the backend supports BLOBs for the entity type.
     *
     * Calls [callback] once before the upload starts and then whenever
     * the state of the upload changes.
     *
     * Blob ID is empty until the upload finishes. The actual id of the blob
     * is set when callback is called with state [BlobCreateState.Done].
     *
     * @param  entityId  Id of the entity the new BLOB belongs to.
     * @param  name      Name of the BLOB, typically the file name.
     * @param  type      Type of the BLOB, typically the MIME type.
     * @param  data      BLOB data, a Javascript Blob.
     * @param  callback  Callback function to report progress, completion or error.
     *
     * @return A BO which contains data of the blob. The `id` is empty in this BO.
     *
     * @throws IllegalArgumentException the bo is invalid (HTTP status code 400)
     * @throws NoSuchElementException if the record with the given id does not exists (HTTP status code 404)
     * @throws DataConflictException the server reported a data conflict (HTTP status code 409)
     * @throws NotImplementedError this function is not implemented on the server side (HTTP status code 501)
     * @throws RuntimeException if there is a general server side processing error (HTTP status code 4xx, 5xx)
     */
    fun blobCreate(
        entityId: EntityId<T>?, name: String, type: String, data: Any,
        callback: (bo: BlobBo, state: BlobCreateState, uploaded: Long) -> Unit
    )

    /**
     * Retrieves metadata of BLOBs.
     *
     * @param  entityId  Id of the record the BLOBs belong to.
     *
     * @return  List of BLOB metadata.
     *
     * @throws IllegalArgumentException the bo is invalid (HTTP status code 400)
     * @throws NoSuchElementException if the record with the given id does not exists (HTTP status code 404)
     * @throws DataConflictException the server reported a data conflict (HTTP status code 409)
     * @throws NotImplementedError this function is not implemented on the server side (HTTP status code 501)
     * @throws RuntimeException if there is a general server side processing error (HTTP status code 4xx, 5xx)
     */
    suspend fun blobMetaList(entityId: EntityId<T>): List<BlobBo>

    /**
     * Retrieves metadata of one BLOB.
     *
     * @param  blobId  Id of the record the BLOBs belong to.
     *
     * @return  List of BLOB metadata.
     *
     * @throws IllegalArgumentException the bo is invalid (HTTP status code 400)
     * @throws NoSuchElementException if the record with the given id does not exists (HTTP status code 404)
     * @throws DataConflictException the server reported a data conflict (HTTP status code 409)
     * @throws NotImplementedError this function is not implemented on the server side (HTTP status code 501)
     * @throws RuntimeException if there is a general server side processing error (HTTP status code 4xx, 5xx)
     */
    suspend fun blobMetaRead(blobId: EntityId<BlobBo>): BlobBo

    /**
     * Updates an metadata of a blob: recordId, name, type fields.
     *
     * @param  bo   The bo to update.
     *
     * @return The updated BO.
     *
     * @throws IllegalArgumentException the bo is invalid (HTTP status code 400)
     * @throws NoSuchElementException if the record with the given id does not exists (HTTP status code 404)
     * @throws DataConflictException the server reported a data conflict (HTTP status code 409)
     * @throws NotImplementedError this function is not implemented on the server side (HTTP status code 501)
     * @throws RuntimeException if there is a general server side processing error (HTTP status code 4xx, 5xx)
     */
    suspend fun blobMetaUpdate(bo: BlobBo): BlobBo

    /**
     * Deletes a BLOB.
     *
     * @param  blobId        Id of the blob to delete.
     *
     * @throws IllegalArgumentException the bo is invalid (HTTP status code 400)
     * @throws NoSuchElementException if the record with the given id does not exists (HTTP status code 404)
     * @throws DataConflictException the server reported a data conflict (HTTP status code 409)
     * @throws NotImplementedError this function is not implemented on the server side (HTTP status code 501)
     * @throws RuntimeException if there is a general server side processing error (HTTP status code 4xx, 5xx)
     */
    suspend fun blobDelete(blobId: EntityId<BlobBo>)
}