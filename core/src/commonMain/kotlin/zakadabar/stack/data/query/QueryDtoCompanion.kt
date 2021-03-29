/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.data.query

import kotlinx.serialization.KSerializer

abstract class QueryDtoCompanion<RESULT : Any>(
    val namespace: String
) {

    abstract fun serializer(): KSerializer<RESULT>

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