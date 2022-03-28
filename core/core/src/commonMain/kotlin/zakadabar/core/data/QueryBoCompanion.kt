/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.data

import zakadabar.core.comm.CommConfig
import zakadabar.core.comm.QueryCommInterface
import zakadabar.core.comm.makeQueryComm
import zakadabar.core.util.use

abstract class QueryBoCompanion(
    val boNamespace: String,
    commConfig : CommConfig? = null
) {

    private var _comm: QueryCommInterface? = null

    var commConfig = commConfig
        get() = CommConfig.configLock.use { field }
        set(value) = CommConfig.configLock.use { field = value }

    private fun makeComm(): QueryCommInterface {
        val nc = makeQueryComm(this)
        _comm = nc
        return nc
    }

    var comm: QueryCommInterface
        get() = _comm ?: makeComm()
        set(value) {
            _comm = value
        }

}