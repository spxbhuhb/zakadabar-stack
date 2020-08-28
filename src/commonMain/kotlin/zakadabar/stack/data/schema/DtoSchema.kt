/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.data.schema

import kotlin.reflect.KClass
import kotlin.reflect.KProperty0

open class DtoSchema {

    companion object {
        fun build(init: DtoSchema.() -> Unit): DtoSchema {
            val schema = DtoSchema()
            schema.init()
            return schema
        }

        val NO_VALIDATION = DtoSchema()
    }

    private val ruleLists = mutableMapOf<KProperty0<*>, ValidationRuleList<*>>()

    operator fun KProperty0<String>.unaryPlus(): StringValidationRuleList {
        val ruleList = StringValidationRuleList(this)
        ruleLists[this] = ruleList
        return ruleList
    }

}

interface ValidationRuleList<T>

interface ValidationRule<T> {
    fun validate(value: String, report: ValidityReport)
}

class ValidityReport(
    val fails: MutableMap<KProperty0<*>, MutableList<KClass<*>>> = mutableMapOf()
) {
    fun fail(property: KProperty0<*>, validation: KClass<*>) {
        fails.getOrPut(property) { mutableListOf() } += validation
    }
}