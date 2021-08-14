/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.data.query

abstract class QueryBoCompanion(
    val boNamespace: String
) {

    private var _comm: QueryCommInterface? = null

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