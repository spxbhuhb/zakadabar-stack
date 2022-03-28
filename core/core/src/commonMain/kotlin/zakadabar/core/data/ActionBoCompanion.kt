/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.data

import zakadabar.core.comm.ActionCommInterface
import zakadabar.core.comm.CommConfig
import zakadabar.core.comm.makeActionComm

abstract class ActionBoCompanion(
    val boNamespace: String,
    val commConfig : CommConfig? = null
) {

    private var _comm: ActionCommInterface? = null

    private fun makeComm(): ActionCommInterface {
        val nc = makeActionComm(this, commConfig)
        _comm = nc
        return nc
    }

    var comm: ActionCommInterface
        get() = _comm ?: makeComm()
        set(value) {
            _comm = value
        }

}