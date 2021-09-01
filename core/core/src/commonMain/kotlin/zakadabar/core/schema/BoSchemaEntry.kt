/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.schema

import zakadabar.core.schema.descriptor.BoConstraint
import zakadabar.core.schema.descriptor.BoProperty

interface BoSchemaEntry<T> {
    fun validate(report: ValidityReport)
    fun isOptional(): Boolean
    fun setDefault()
    fun decodeFromText(text : String?) : T { throw NotImplementedError() }
    fun setFromText(text : String?) { throw NotImplementedError() }
    fun push(bo: BoProperty)
    fun toBoProperty(): BoProperty?
    fun constraints(): List<BoConstraint>
}