/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.data.action

import kotlinx.serialization.KSerializer

abstract class ActionBoCompanion<RESPONSE : Any>(
    val boNamespace: String
) {

    abstract fun serializer(): KSerializer<RESPONSE>

    private var _comm: ActionCommInterface? = null

    private fun makeComm(): ActionCommInterface {
        val nc = makeActionComm(this)
        _comm = nc
        return nc
    }

    var comm: ActionCommInterface
        get() = _comm ?: makeComm()
        set(value) {
            _comm = value
        }

}