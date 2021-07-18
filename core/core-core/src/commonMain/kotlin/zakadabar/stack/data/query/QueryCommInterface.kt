/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.data.query

import kotlinx.serialization.KSerializer

interface QueryCommInterface {

    /**
     * Runs a query on the server with the given parameters and returns a list of BOs.
     *
     * @param   request            The query request to send.
     * @param   requestSerializer  Serializer for the request.
     * @param   responseSerializer Serializer for the response.
     *
     * @return  List of BOs.
     *
     * // TODO add throws docs
     */
    suspend fun <REQUEST : Any, RESPONSE> query(request: REQUEST, requestSerializer: KSerializer<REQUEST>, responseSerializer: KSerializer<RESPONSE>): RESPONSE

}