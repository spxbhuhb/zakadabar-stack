/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.data.query

import kotlinx.serialization.KSerializer
import zakadabar.stack.data.record.RecordDtoCompanion

abstract class QueryDtoCompanion(
    val base: RecordDtoCompanion<*>
) {
    abstract fun serializer(): KSerializer<Any>
}