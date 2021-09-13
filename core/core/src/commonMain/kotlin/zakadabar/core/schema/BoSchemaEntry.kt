/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.schema

import zakadabar.core.schema.descriptor.BoConstraint
import zakadabar.core.schema.descriptor.BoProperty
import kotlin.reflect.KProperty

interface BoSchemaEntry<T,ET : BoSchemaEntry<T,ET>> {
    val propName : String get() = kProperty.name
    val kProperty : KProperty<T>
    val rules : MutableList<BoPropertyConstraintImpl<T>>
    val extensions : MutableList<BoSchemaEntryExtension<T>>
    var defaultValue : T
    fun validate(report: ValidityReport)
    fun isOptional(): Boolean
    fun setDefault()
    fun decodeFromText(text : String?) : T { throw NotImplementedError() }
    fun setFromText(text : String?) { throw NotImplementedError() }
    fun push(bo: BoProperty)
    fun toBoProperty(): BoProperty?
    fun constraints(): List<BoConstraint>
}