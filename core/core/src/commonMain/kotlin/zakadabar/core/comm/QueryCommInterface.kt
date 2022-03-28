/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.comm

import kotlinx.serialization.KSerializer
import zakadabar.core.authorize.Executor

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
     * @param   executor           The executor of the call (used by local calls to authorize the call).
     * @param   config             Configuration for the communication, uses class or global defaults (in that order) when null.
     *
     * @return  Result of the query.
     *
     * // TODO add throws docs
     * @throws  NoSuchElementException  when the server returns with "null"
     */
    suspend fun <REQUEST : Any, RESPONSE : Any> query(
        request: REQUEST,
        requestSerializer: KSerializer<REQUEST>,
        responseSerializer: KSerializer<RESPONSE>,
        executor: Executor? = null,
        config: CommConfig ? = null
    ): RESPONSE

    /**
     * Runs a query on the server with the given parameters and returns a list of BOs.
     *
     * @param   request            The query request to send.
     * @param   requestSerializer  Serializer for the request.
     * @param   responseSerializer Serializer for the response.
     * @param   config             Configuration for the communication, uses class or global defaults (in that order) when null.
     *
     * @return  Result of the query, may be null.
     *
     * // TODO add throws docs
     */
    suspend fun <REQUEST : Any, RESPONSE : Any?> queryOrNull(
        request: REQUEST,
        requestSerializer: KSerializer<REQUEST>,
        responseSerializer: KSerializer<RESPONSE>,
        executor: Executor? = null,
        config: CommConfig ? = null
    ): RESPONSE?

}