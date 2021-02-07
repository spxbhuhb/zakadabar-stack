/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.data.query

import kotlinx.serialization.KSerializer
import zakadabar.stack.data.util.AuthenticationException
import zakadabar.stack.data.util.AuthorizationException
import zakadabar.stack.data.util.CommunicationException

interface QueryCommInterface {

    /**
     * Runs a query on the server with the given parameters and returns a list of DTOs which are
     * different than the record DTO.
     *
     * Use this function when the return type is significantly different than the record DTO. For
     * example, you want to display compound data in a table which collects information from
     * a number of different records.
     *
     * @param   request            The query request to send.
     * @param   requestSerializer  Serializer for the request.
     * @param   responseSerializer Serializer for the response.
     *
     * @return  List of DTOs.
     *
     * @throws IllegalArgumentException the request dto is invalid (HTTP status code 400)
     * @throws AuthenticationException the principal is not authenticated (HTTP status code 401)
     * @throws AuthorizationException the principal is authenticated but not authorized to create objects of this type (HTTP status code 403)
     * @throws NotImplementedError this function is not implemented on the server side (HTTP status code 501)
     * @throws RuntimeException if there is a general server side processing error (HTTP status code 5xx)
     * @throws CommunicationException server unavailable, request timeout, any not 400,401,403 error codes
     */
    suspend fun <REQUEST : Any, RESPONSE> query(request: REQUEST, requestSerializer: KSerializer<REQUEST>, responseSerializer: KSerializer<List<RESPONSE>>): List<RESPONSE>

}