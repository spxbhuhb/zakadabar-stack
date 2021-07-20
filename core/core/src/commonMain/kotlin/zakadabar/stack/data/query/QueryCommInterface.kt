/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.data.query

import kotlinx.serialization.KSerializer

interface QueryCommInterface {

    /**
     * Runs a query on the server with the given parameters and returns a list of BOs.
     *
     * This version of the method throws NoSuchElementException if the server returns
     * with "null".
     *
     * @param   request            The query request to send.
     * @param   requestSerializer  Serializer for the request.
     * @param   responseSerializer Serializer for the response.
     *
     * @return  Result of the query.
     *
     * // TODO add throws docs
     * @throws  NoSuchElementException  when the server returns with "null"
     */
    suspend fun <REQUEST : Any, RESPONSE : Any> query(request: REQUEST, requestSerializer: KSerializer<REQUEST>, responseSerializer: KSerializer<RESPONSE>): RESPONSE

    /**
     * Runs a query on the server with the given parameters and returns a list of BOs.
     *
     * @param   request            The query request to send.
     * @param   requestSerializer  Serializer for the request.
     * @param   responseSerializer Serializer for the response.
     *
     * @return  Result of the query, may be null.
     *
     * // TODO add throws docs
     */
    suspend fun <REQUEST : Any, RESPONSE : Any?> queryOrNull(request: REQUEST, requestSerializer: KSerializer<REQUEST>, responseSerializer: KSerializer<RESPONSE>): RESPONSE?

}