/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.data.schema

import kotlinx.datetime.Instant
import zakadabar.stack.util.PublicApi
import kotlin.reflect.KMutableProperty0
import kotlin.reflect.KProperty0

open class DtoSchema() {

    companion object {
        val NO_VALIDATION = DtoSchema()
    }

    constructor(init: DtoSchema.() -> Unit) : this() {
        this.init()
    }

    @Suppress("MemberVisibilityCanBePrivate") // To make extensions possible
    val ruleLists = mutableMapOf<KProperty0<*>, ValidationRuleList<*>>()

    operator fun KMutableProperty0<String>.unaryPlus(): StringValidationRuleList {
        val ruleList = StringValidationRuleList(this)
        ruleLists[this] = ruleList
        return ruleList
    }

    operator fun KMutableProperty0<String?>.unaryPlus(): OptStringValidationRuleList {
        val ruleList = OptStringValidationRuleList(this)
        ruleLists[this] = ruleList
        return ruleList
    }

    operator fun KMutableProperty0<Boolean>.unaryPlus(): BooleanValidationRuleList {
        val ruleList = BooleanValidationRuleList(this)
        ruleLists[this] = ruleList
        return ruleList
    }

    operator fun KMutableProperty0<Boolean?>.unaryPlus(): OptBooleanValidationRuleList {
        val ruleList = OptBooleanValidationRuleList(this)
        ruleLists[this] = ruleList
        return ruleList
    }

    operator fun KMutableProperty0<Int>.unaryPlus(): IntValidationRuleList {
        val ruleList = IntValidationRuleList(this)
        ruleLists[this] = ruleList
        return ruleList
    }

    operator fun KMutableProperty0<Int?>.unaryPlus(): OptIntValidationRuleList {
        val ruleList = OptIntValidationRuleList(this)
        ruleLists[this] = ruleList
        return ruleList
    }

    operator fun KMutableProperty0<Long>.unaryPlus(): LongValidationRuleList {
        val ruleList = LongValidationRuleList(this)
        ruleLists[this] = ruleList
        return ruleList
    }

    operator fun KMutableProperty0<Long?>.unaryPlus(): OptLongValidationRuleList {
        val ruleList = OptLongValidationRuleList(this)
        ruleLists[this] = ruleList
        return ruleList
    }

    operator fun KMutableProperty0<Double>.unaryPlus(): DoubleValidationRuleList {
        val ruleList = DoubleValidationRuleList(this)
        ruleLists[this] = ruleList
        return ruleList
    }

    operator fun KMutableProperty0<Double?>.unaryPlus(): OptDoubleValidationRuleList {
        val ruleList = OptDoubleValidationRuleList(this)
        ruleLists[this] = ruleList
        return ruleList
    }

    operator fun KMutableProperty0<Instant>.unaryPlus(): InstantValidationRuleList {
        val ruleList = InstantValidationRuleList(this)
        ruleLists[this] = ruleList
        return ruleList
    }

    operator fun KMutableProperty0<Instant?>.unaryPlus(): OptInstantValidationRuleList {
        val ruleList = OptInstantValidationRuleList(this)
        ruleLists[this] = ruleList
        return ruleList
    }

    inline operator fun <reified E : Enum<E>> KMutableProperty0<E>.unaryPlus(): EnumValidationRuleList<E> {
        val ruleList = EnumValidationRuleList(this, enumValues<E>().first())
        ruleLists[this] = ruleList
        return ruleList
    }

    @PublicApi
    fun validate(): ValidityReport {
        val report = ValidityReport()
        ruleLists.forEach { it.value.validate(report) }
        return report
    }

    fun setDefaults() {
        ruleLists.forEach { it.value.setDefault() }
    }

    fun isOptional(propName: String): Boolean {
        ruleLists.forEach {
            if (it.key.name == propName) {
                return it.value.isOptional()
            }
        }
        return true
    }

}

interface ValidationRuleList<T> {
    fun validate(report: ValidityReport)
    fun isOptional(): Boolean
    fun setDefault()
}

interface ValidationRule<T> {
    fun validate(value: T, report: ValidityReport)
}

class ValidityReport(
    @PublicApi
    val fails: MutableMap<String, MutableList<ValidationRule<*>>> = mutableMapOf()
) {
    fun fail(property: KProperty0<*>, validation: ValidationRule<*>) {
        fails.getOrPut(property.name) { mutableListOf() } += validation
    }

    @PublicApi
    fun dump(): String {
        val lines = mutableListOf<String>()
        fails.forEach { lines += "${it.key} : ${it.value.map { v -> v::class.simpleName }.joinToString(", ")}" }
        return lines.joinToString("\n")
    }
}