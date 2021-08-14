/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.data.entity

import zakadabar.core.exception.DataConflict

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
     * @throws DataConflict the server reported a data conflict (HTTP status code 409)
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
     * @throws DataConflict the server reported a data conflict (HTTP status code 409)
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
     * @throws DataConflict the server reported a data conflict (HTTP status code 409)
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
     * @throws DataConflict the server reported a data conflict (HTTP status code 409)
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
     * @throws DataConflict the server reported a data conflict (HTTP status code 409)
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
     * @throws DataConflict the server reported a data conflict (HTTP status code 409)
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
     * @throws DataConflict the server reported a data conflict (HTTP status code 409)
     * @throws NotImplementedError this function is not implemented on the server side (HTTP status code 501)
     * @throws RuntimeException if there is a general server side processing error (HTTP status code 4xx, 5xx)
     */
    suspend fun all(): List<T>

}