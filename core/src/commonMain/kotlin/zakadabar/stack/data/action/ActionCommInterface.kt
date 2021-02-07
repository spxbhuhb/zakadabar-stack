/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.data.action

import kotlinx.serialization.KSerializer
import zakadabar.stack.data.util.AuthenticationException
import zakadabar.stack.data.util.AuthorizationException
import zakadabar.stack.data.util.CommunicationException

interface ActionCommInterface {
    /**
     * Executes an action. Use this method when there is complex business logic behind the
     * executed action. For simple record updates use the record comm interface.
     *
     * Complex business logic means whatever that affects more than one record and should
     * be done in one transaction. Technically you could use two record comm calls to
     * achieve the same result but that blows ACID out the window.
     *
     * In REST terminology the action is like posting an "action" resource.
     *
     * Actions use HTTP POST.
     *
     * @param   request              The action parameters to send.
     * @param   requestSerializer    Serializer for the request.
     * @param   responseSerializer   Serializer for the response.
     *
     * @return  The response send by the server.
     *
     * @throws IllegalArgumentException the request dto is invalid (HTTP status code 400)
     * @throws AuthenticationException the principal is not authenticated (HTTP status code 401)
     * @throws AuthorizationException the principal is authenticated but not authorized to create objects of this type (HTTP status code 403)
     * @throws NotImplementedError this function is not implemented on the server side (HTTP status code 501)
     * @throws RuntimeException if there is a general server side processing error (HTTP status code 5xx)
     * @throws CommunicationException server unavailable, request timeout, any not 400,401,403 error codes
     */
    suspend fun <REQUEST : Any, RESPONSE> action(request: REQUEST, requestSerializer: KSerializer<REQUEST>, responseSerializer: KSerializer<RESPONSE>): RESPONSE
}