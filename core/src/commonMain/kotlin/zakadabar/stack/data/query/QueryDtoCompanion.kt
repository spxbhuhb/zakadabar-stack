/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.data.query

import kotlinx.serialization.KSerializer

abstract class QueryDtoCompanion<RESULT : Any> {

    abstract fun serializer(): KSerializer<RESULT>

    lateinit var comm: () -> QueryCommInterface

}