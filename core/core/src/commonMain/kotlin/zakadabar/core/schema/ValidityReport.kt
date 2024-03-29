/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.schema

import kotlinx.serialization.Serializable
import zakadabar.core.data.BaseBo
import zakadabar.core.schema.descriptor.BoConstraint
import zakadabar.core.schema.descriptor.CustomBoConstraint
import zakadabar.core.util.PublicApi
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty0 as KProperty01

@Serializable
class ValidityReport(
    val allowEmptyId: Boolean = false,
    val fails: MutableMap<String, MutableList<BoConstraint>> = mutableMapOf()
) : BaseBo {

    fun fail(property: KProperty<*>, customConstraintName : String) : ValidityReport {
        fails.getOrPut(property.name) { mutableListOf() } += CustomBoConstraint(name = customConstraintName)
        return this
    }

    fun fail(property: KProperty01<*>, constraintImpl: BoPropertyConstraintImpl<*>) {
        fails.getOrPut(property.name) { mutableListOf() } += constraintImpl.toBoConstraint()
    }

    fun fail(property: KProperty01<*>, constraint: BoConstraint) {
        fails.getOrPut(property.name) { mutableListOf() } += constraint
    }

    @PublicApi
    fun dump(): String {
        val lines = mutableListOf<String>()
        fails.forEach { lines += "${it.key} : ${it.value.map { c -> c.toString() }.joinToString(", ")}" }
        return lines.joinToString("\n")
    }
}