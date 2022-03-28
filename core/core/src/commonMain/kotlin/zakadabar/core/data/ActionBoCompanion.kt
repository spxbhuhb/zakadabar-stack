/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.data

import zakadabar.core.comm.ActionCommInterface
import zakadabar.core.comm.CommConfig
import zakadabar.core.comm.makeActionComm
import zakadabar.core.util.use

abstract class ActionBoCompanion(
    val boNamespace: String,
    commConfig : CommConfig? = null
) {

    private var _comm: ActionCommInterface? = null

    var commConfig = commConfig
        get() = CommConfig.configLock.use { field }
        set(value) = CommConfig.configLock.use { field = value }

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