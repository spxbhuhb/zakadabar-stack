/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.comm

import kotlinx.serialization.KSerializer
import zakadabar.core.authorize.Executor

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
     * @param   executor             The executor of the call (used by local calls to authorize the call).
     * @param   config               Configuration for the communication, uses class or global defaults (in that order) when null.
     *
     * @return  The response sent by the server.
     *
     * // TODO add throws docs
     */
    suspend fun <REQUEST : Any, RESPONSE> action(
        request: REQUEST,
        requestSerializer: KSerializer<REQUEST>,
        responseSerializer: KSerializer<RESPONSE>,
        executor: Executor? = null,
        config: CommConfig ? = null
    ): RESPONSE

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
     * @param   executor             The executor of the call (used by local calls to authorize the call).
     * @param   config               Configuration for the communication, uses class or global defaults (in that order) when null.
     *
     * @return  The response sent by the server or null.
     *
     * // TODO add throws docs
     */
    suspend fun <REQUEST : Any, RESPONSE> actionOrNull(
        request: REQUEST,
        requestSerializer: KSerializer<REQUEST>,
        responseSerializer: KSerializer<RESPONSE>,
        executor: Executor? = null,
        config: CommConfig ? = null
    ): RESPONSE?
}