/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.comm

import zakadabar.core.authorize.Executor
import zakadabar.core.data.EntityId
import zakadabar.core.exception.DataConflict

/**
 * Interface to be implemented on the client side for communication.
 */
interface EntityCommInterface<T> {

    /**
     * Creates a new entity on the server.
     *
     * @param  bo        BO of the entity to create.
     * @param  executor  The executor of the call (used by local calls to authorize the call).
     * @param  config    Configuration for the communication, uses class or global defaults (in that order) when null.
     *
     * @return The BO of the created entity.
     *
     * @throws IllegalArgumentException the bo is invalid (HTTP status code 400)
     * @throws DataConflict the server reported a data conflict (HTTP status code 409)
     * @throws NotImplementedError this function is not implemented on the server side (HTTP status code 501)
     * @throws RuntimeException if there is a general server side processing error (HTTP status code 4xx, 5xx)
     */
    suspend fun create(bo: T, executor: Executor? = null, config: CommConfig? = null): T

    /**
     * Fetches an entity.
     *
     * @param  id  Id of the entity.
     * @param  executor  The executor of the call (used by local calls to authorize the call).
     * @param  config  Configuration for the communication, uses class or global defaults (in that order) when null.
     *
     * @return  BO of the fetched entity.
     *
     * @throws IllegalArgumentException the bo is invalid (HTTP status code 400)
     * @throws NoSuchElementException if the record with the given id does not exists (HTTP status code 404)
     * @throws DataConflict the server reported a data conflict (HTTP status code 409)
     * @throws NotImplementedError this function is not implemented on the server side (HTTP status code 501)
     * @throws RuntimeException if there is a general server side processing error (HTTP status code 4xx, 5xx)
     */
    suspend fun read(id: EntityId<T>, executor: Executor? = null, config: CommConfig? = null): T

    /**
     * Fetches an entity.
     *
     * @param  id  Id of the entity.
     * @param  executor  The executor of the call (used by local calls to authorize the call).
     * @param  config  Configuration for the communication, uses class or global defaults (in that order) when null.
     *
     * @return  BO of the fetched entity.
     *
     * @throws IllegalArgumentException the bo is invalid (HTTP status code 400)
     * @throws NoSuchElementException if the record with the given id does not exists (HTTP status code 404)
     * @throws DataConflict the server reported a data conflict (HTTP status code 409)
     * @throws NotImplementedError this function is not implemented on the server side (HTTP status code 501)
     * @throws RuntimeException if there is a general server side processing error (HTTP status code 4xx, 5xx)
     */
    suspend fun read(id: Long, executor: Executor? = null, config: CommConfig? = null) = read(EntityId(id))

    /**
     * Fetches an entity.
     *
     * @param  id  Id of the entity.
     * @param  executor  The executor of the call (used by local calls to authorize the call).
     * @param  config  Configuration for the communication, uses class or global defaults (in that order) when null.
     *
     * @return  BO of the fetched entity.
     *
     * @throws IllegalArgumentException the bo is invalid (HTTP status code 400)
     * @throws NoSuchElementException if the record with the given id does not exists (HTTP status code 404)
     * @throws DataConflict the server reported a data conflict (HTTP status code 409)
     * @throws NotImplementedError this function is not implemented on the server side (HTTP status code 501)
     * @throws RuntimeException if there is a general server side processing error (HTTP status code 4xx, 5xx)
     */
    suspend fun read(id: String, executor: Executor? = null, config: CommConfig? = null) = read(EntityId(id))

    /**
     * Updates an entity on the server.
     *
     * @param  bo  BO of the entity to update.
     * @param  executor  The executor of the call (used by local calls to authorize the call).
     * @param  config  Configuration for the communication, uses class or global defaults (in that order) when null.
     *
     * @return The updated BO.
     *
     * @throws IllegalArgumentException the bo is invalid (HTTP status code 400)
     * @throws NoSuchElementException if the record with the given id does not exists (HTTP status code 404)
     * @throws DataConflict the server reported a data conflict (HTTP status code 409)
     * @throws NotImplementedError this function is not implemented on the server side (HTTP status code 501)
     * @throws RuntimeException if there is a general server side processing error (HTTP status code 4xx, 5xx)
     */
    suspend fun update(bo: T, executor: Executor? = null, config: CommConfig? = null): T

    /**
     * Deletes an entity on the server.
     *
     * @param  id  Id of the entity to delete.
     * @param  executor  The executor of the call (used by local calls to authorize the call).
     * @param  config  Configuration for the communication, uses class or global defaults (in that order) when null.
     *
     * @throws IllegalArgumentException the bo is invalid (HTTP status code 400)
     * @throws NoSuchElementException if the record with the given id does not exists (HTTP status code 404)
     * @throws DataConflict the server reported a data conflict (HTTP status code 409)
     * @throws NotImplementedError this function is not implemented on the server side (HTTP status code 501)
     * @throws RuntimeException if there is a general server side processing error (HTTP status code 4xx, 5xx)
     */
    suspend fun delete(id: EntityId<T>, executor: Executor? = null, config: CommConfig? = null)

    /**
     * Retrieves BOs of all entities of the given type.
     *
     * @param  executor  The executor of the call (used by local calls to authorize the call).
     * @param  config  Configuration for the communication, uses class or global defaults (in that order) when null.
     *
     * @return  List of entity BOs.
     *
     * @throws IllegalArgumentException the bo is invalid (HTTP status code 400)
     * @throws NoSuchElementException if the record with the given id does not exists (HTTP status code 404)
     * @throws DataConflict the server reported a data conflict (HTTP status code 409)
     * @throws NotImplementedError this function is not implemented on the server side (HTTP status code 501)
     * @throws RuntimeException if there is a general server side processing error (HTTP status code 4xx, 5xx)
     */
    suspend fun all(executor: Executor? = null, config: CommConfig? = null): List<T>

}