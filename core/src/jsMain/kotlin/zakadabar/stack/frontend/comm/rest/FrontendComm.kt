/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.comm.rest

import kotlinx.browser.window
import kotlinx.coroutines.await
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import org.w3c.fetch.Headers
import org.w3c.fetch.RequestInit
import zakadabar.stack.comm.http.Comm
import zakadabar.stack.data.record.RecordDto
import zakadabar.stack.frontend.errors.FetchError
import zakadabar.stack.frontend.errors.ensure
import zakadabar.stack.frontend.util.json
import zakadabar.stack.util.PublicApi

/**
 * REST communication functions for objects that implement [RecordDto]
 *
 *
 * @property  path  The path on which the server provides the REST
 *                  access to this data store, for example "/apis/1a2b3c".
 *
 * @property  serializer  The serializer to serialize/deserialize objects
 *                        sent/received.
 */
@PublicApi
open class FrontendComm<T : RecordDto<T>>(
    internal val path: String,
    private val serializer: KSerializer<T>
) : Comm<T> {

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
     * Fetches an object.
     *
     * @param  id  Id of the object.
     *
     * @return  The DTO fetched.
     *
     * @throws  FetchError
     */
    @PublicApi
    override suspend fun read(id: Long): T {
        val responsePromise = window.fetch("/api/$path/$id")
        val response = responsePromise.await()

        ensure(response.ok) { FetchError(response) }

        val textPromise = response.text()
        val text = textPromise.await()

        return json.decodeFromString(serializer, text)
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

    /**
     * Retrieves all objects.
     *
     * @return  The response send by the server.
     *
     * @throws  FetchError
     */
    @PublicApi
    override suspend fun all(): List<T> {

        val responsePromise = window.fetch("/api/$path/all")
        val response = responsePromise.await()

        ensure(response.ok) { FetchError(response) }

        val textPromise = response.text()
        val text = textPromise.await()

        return json.decodeFromString(ListSerializer(serializer), text)
    }

    /**
     * Deletes an object.
     *
     * @param  id  Id of the object to delete.
     *
     * @throws  FetchError
     */
    @PublicApi
    override suspend fun delete(id: Long) {

        val responsePromise = window.fetch("/api/$path/$id", RequestInit(method = "DELETE"))
        val response = responsePromise.await()

        ensure(response.ok) { FetchError(response) }
    }

    /**
     * Searches for objects by the passed search parameters.
     *
     * @param   request            The query request to send.
     * @param   requestSerializer  Serializer for the request.
     *
     * @return  List of objects found.
     *
     * @throws  FetchError
     */
    override suspend fun <RQ : Any> query(request: RQ, requestSerializer: KSerializer<RQ>) =
        query(request, requestSerializer, ListSerializer(serializer))

    /**
     * Runs a generic query.
     *
     * @param   request  The query request to send.
     * @param   requestSerializer  Serializer for the request.
     * @param   responseSerializer   Serializer for the response.
     *
     * @return  The response send by the server.
     *
     * @throws  FetchError
     */
    @PublicApi
    override suspend fun <RQ : Any, RS> query(request: RQ, requestSerializer: KSerializer<RQ>, responseSerializer: KSerializer<RS>): RS {

        val responsePromise = window.fetch("/api/$path/${request::class.simpleName}?q=${Json.encodeToString(requestSerializer, request)}")
        val response = responsePromise.await()

        ensure(response.ok) { FetchError(response) }

        val textPromise = response.text()
        val text = textPromise.await()

        return json.decodeFromString(responseSerializer, text)
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