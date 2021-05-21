/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.data.query

import kotlinx.serialization.KSerializer


abstract class QueryBoCompanion<RESULT : Any>(
    val boNamespace: String
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

@Deprecated("EOL: 2021.6.30 - use QueryBoCompanion instead", ReplaceWith("QueryBoCompanion"))
abstract class QueryDtoCompanion<RESULT : Any>(
    dtoNamespace : String
) : QueryBoCompanion<RESULT>(dtoNamespace)