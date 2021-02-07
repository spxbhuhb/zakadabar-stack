/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.data.query

import kotlinx.serialization.KSerializer
import zakadabar.stack.data.record.RecordDto

abstract class QueryDtoCompanion<COMM : RecordDto<COMM>, RESULT : Any> {

    abstract fun serializer(): KSerializer<RESULT>

    lateinit var comm: () -> QueryCommInterface

}