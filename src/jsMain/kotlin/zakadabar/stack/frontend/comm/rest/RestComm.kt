/*
 * Copyright © 2020, Simplexion, Hungary
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package zakadabar.stack.frontend.comm.rest

import kotlinx.browser.window
import kotlinx.coroutines.await
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import org.w3c.fetch.Headers
import org.w3c.fetch.RequestInit
import zakadabar.stack.extend.DtoWithIdContract
import zakadabar.stack.extend.RestCommContract
import zakadabar.stack.frontend.errors.FetchError
import zakadabar.stack.frontend.errors.ensure
import zakadabar.stack.frontend.util.json
import zakadabar.stack.util.PublicApi

/**
 * REST communication functions for objects that implement [DtoWithIdContract]
 *
 * ```kotlin
 *   // set up FolderDTO REST communication
 *
 *   FolderDTO.comm = RestComm("/apis/1a2b3c", FolderDTO.serializer)
 *
 *   // create a new folder (on the server)
 *
 *   val folder = FolderDTO(...).create()
 *
 *   // update the folder we've just crated (sends update server)
 *
 *   val updated = folder.copy(name = "bello").update()
 *
 *   // get a folder with id 12 from the server
 *
 *   val folder12 = FolderDTO.get(12L)
 *
 * ```
 *
 * @property  path  The path on which the server provides the REST
 *                  access to this data store, for example "/apis/1a2b3c".
 *
 * @property  serializer  The serializer to serialize/deserialize objects
 *                        sent/received.
 */
@PublicApi
open class RestComm<T : DtoWithIdContract<T>>(
    private val path: String,
    private val serializer: KSerializer<T>
) : RestCommContract<T> {

    /**
     * Fetches an object.
     *
     * @param  id  Id of the object.
     *
     * @return  The DTO fetched.
     *
     * @throws  FetchError
     */
    @PublicApi
    override suspend fun get(id: Long): T {
        val responsePromise = window.fetch("/api/$path/$id")
        val response = responsePromise.await()

        ensure(response.ok) { FetchError(response) }

        val textPromise = response.text()
        val text = textPromise.await()

        return json.decodeFromString(serializer, text)
    }

    /**
     * Fetches an object or returns with null if the passed id is null.
     * Errors reported by the server still throw exceptions.
     *
     * @param  id  Id of the object.
     *
     * @return  The DTO fetched or null.
     *
     * @throws  FetchError
     */
    override suspend fun get(id: Long?) = if (id == null) null else get(id)

    /**
     * Fetches children of an object.
     *
     * @param  parentId  Id of the parent object.
     *
     * @return  The list of objects fetched.
     *
     * @throws  FetchError
     */
    @PublicApi
    override suspend fun getChildren(parentId: Long?): List<T> {

        val responsePromise = window.fetch("/api/" + if (parentId == null) path else "$path?parent=$parentId")
        val response = responsePromise.await()

        ensure(response.ok) { FetchError(response) }

        val textPromise = response.text()
        val text = textPromise.await()

        return json.decodeFromString(ListSerializer(serializer), text)
    }

    /**
     * Searches for object by the passes search parameters.
     *
     * @param   parameters  The query parameters to pass.
     *
     * @return  The list of DTOs fetched.
     *
     * @throws  FetchError
     */
    @PublicApi
    override suspend fun search(parameters: String): List<T> {

        val responsePromise = window.fetch("/api/$path?$parameters")
        val response = responsePromise.await()

        ensure(response.ok) { FetchError(response) }

        val textPromise = response.text()
        val text = textPromise.await()

        return json.decodeFromString(ListSerializer(serializer), text)
    }

    /**
     * Creates a new object on the server.
     *
     * @param  dto  DTO of the object to create.
     *
     * @throws  FetchError
     */
    override suspend fun create(dto: T): T {
        ensure(dto.id == 0L) { "id is not 0 in $dto" }

        val headers = Headers()

        headers.append("content-type", "application/json")

        val body = json.encodeToString(serializer, dto)

        val requestInit = RequestInit(
            method = "POST",
            headers = headers,
            body = body
        )

        return sendAndReceive(path, requestInit)
    }

    /**
     * Updates an object on the server.
     *
     * @param  dto  DTO of the object to update.
     *
     * @throws  FetchError
     */
    override suspend fun update(dto: T): T {
        ensure(dto.id != 0L) { "id is 0 in $dto" }

        val headers = Headers()

        headers.append("content-type", "application/json")

        val body = json.encodeToString(serializer, dto)

        val requestInit = RequestInit(
            method = "PATCH",
            headers = headers,
            body = body
        )

        return sendAndReceive(path, requestInit)
    }

    private suspend fun sendAndReceive(path: String, requestInit: RequestInit): T {
        val responsePromise = window.fetch("/api/$path", requestInit)
        val response = responsePromise.await()

        ensure(response.ok) { FetchError(response) }

        val textPromise = response.text()
        val text = textPromise.await()

        return json.decodeFromString(serializer, text)
    }
}