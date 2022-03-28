/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.data

import zakadabar.core.comm.CommConfig
import zakadabar.core.comm.QueryCommInterface
import zakadabar.core.comm.makeQueryComm

abstract class QueryBoCompanion(
    val boNamespace: String,
    val commConfig : CommConfig? = null
) {

    private var _comm: QueryCommInterface? = null

    private fun makeComm(): QueryCommInterface {
        val nc = makeQueryComm(this, commConfig)
        _comm = nc
        return nc
    }

    var comm: QueryCommInterface
        get() = _comm ?: makeComm()
        set(value) {
            _comm = value
        }

}