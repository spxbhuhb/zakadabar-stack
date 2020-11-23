/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.data.schema

import zakadabar.stack.util.PublicApi
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

    @PublicApi
    fun validate() : ValidityReport {
        val report = ValidityReport()
        ruleLists.forEach { it.value.validate(report) }
        return report
    }

}

interface ValidationRuleList<T> {
    fun validate(report: ValidityReport)
}

interface ValidationRule<T> {
    fun validate(value: T, report: ValidityReport)
}

class ValidityReport(
    @PublicApi
    val fails: MutableMap<String, MutableList<ValidationRule<*>>> = mutableMapOf()
) {
    fun fail(property: KProperty0<*>, validation: ValidationRule<*>) {
        println("fail: ${validation::class.simpleName}")
        fails.getOrPut(property.name) { mutableListOf() } += validation
    }

    @PublicApi
    fun dump() : String {
        val lines = mutableListOf<String>()
        fails.forEach { lines += "${it.key} : ${it.value.map { v -> v::class.simpleName }.joinToString(", ")}" }
        return lines.joinToString("\n")
    }
}