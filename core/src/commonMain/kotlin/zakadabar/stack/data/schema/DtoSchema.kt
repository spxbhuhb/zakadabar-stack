/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.data.schema

import kotlinx.datetime.Instant
import zakadabar.stack.data.DtoBase
import zakadabar.stack.data.builtin.misc.Secret
import zakadabar.stack.data.record.RecordId
import zakadabar.stack.data.schema.dto.DescriptorDto
import zakadabar.stack.data.schema.dto.PropertyDto
import zakadabar.stack.data.schema.dto.ValidationDto
import zakadabar.stack.data.schema.validations.*
import zakadabar.stack.util.PublicApi
import zakadabar.stack.util.UUID
import kotlin.js.JsName
import kotlin.reflect.KMutableProperty0
import kotlin.reflect.KProperty0

/**
 * A data model schema that may be used to validate a data instance.
 */
open class DtoSchema() {

    companion object {
        val NO_VALIDATION = DtoSchema()
    }

    constructor(init: DtoSchema.() -> Unit) : this() {
        this.init()
    }

    @Suppress("MemberVisibilityCanBePrivate") // To make extensions possible
    val ruleLists = mutableMapOf<KMutableProperty0<*>, ValidationRuleList<*>>()

    @Suppress("MemberVisibilityCanBePrivate") // To make extensions possible
    val customRuleLists = mutableListOf<CustomValidationRuleList>()

    operator fun KMutableProperty0<out RecordId<*>>.unaryPlus(): RecordIdValidationRuleList {
        @Suppress("UNCHECKED_CAST") // at this point out doesn't really matters
        val ruleList = RecordIdValidationRuleList(this as KMutableProperty0<RecordId<*>>)
        ruleLists[this] = ruleList
        return ruleList
    }

    operator fun KMutableProperty0<out RecordId<*>?>.unaryPlus(): OptRecordIdValidationRuleList {
        @Suppress("UNCHECKED_CAST") // at this point out doesn't really matters
        val ruleList = OptRecordIdValidationRuleList(this as KMutableProperty0<RecordId<*>?>)
        ruleLists[this] = ruleList
        return ruleList
    }

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

    operator fun KMutableProperty0<Secret>.unaryPlus(): SecretValidationRuleList {
        val ruleList = SecretValidationRuleList(this)
        ruleLists[this] = ruleList
        return ruleList
    }

    operator fun KMutableProperty0<Secret?>.unaryPlus(): OptSecretValidationRuleList {
        val ruleList = OptSecretValidationRuleList(this)
        ruleLists[this] = ruleList
        return ruleList
    }

    operator fun KMutableProperty0<UUID>.unaryPlus(): UuidValidationRuleList {
        val ruleList = UuidValidationRuleList(this)
        ruleLists[this] = ruleList
        return ruleList
    }

    operator fun KMutableProperty0<UUID?>.unaryPlus(): OptUuidValidationRuleList {
        val ruleList = OptUuidValidationRuleList(this)
        ruleLists[this] = ruleList
        return ruleList
    }

    inline operator fun <reified T : DtoBase> KMutableProperty0<T>.unaryPlus(): DtoBaseValidationRuleList<T> {
        val ruleList = DtoBaseValidationRuleList(this)
        ruleLists[this] = ruleList
        return ruleList
    }

    @JsName("SchemaOptEnumUnaryPlus")
    inline operator fun <reified E : Enum<E>> KMutableProperty0<E>.unaryPlus(): EnumValidationRuleList<E> {
        val ruleList = EnumValidationRuleList(this, enumValues())
        ruleLists[this] = ruleList
        return ruleList
    }

    inline operator fun <reified E : Enum<E>> KMutableProperty0<E?>.unaryPlus(): OptEnumValidationRuleList<E> {
        val ruleList = OptEnumValidationRuleList(this, enumValues())
        ruleLists[this] = ruleList
        return ruleList
    }

    operator fun plusAssign(custom: CustomValidationRuleList) {
        customRuleLists += custom
    }

    operator fun CustomValidationRuleList.unaryPlus() {
        customRuleLists += this
    }

    fun custom(function: (report: ValidityReport, rule: ValidationRule<Unit>) -> Unit) = CustomValidationRuleList(function)

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

    fun toDescriptorDto() = DescriptorDto(ruleLists.mapNotNull { it.value.toPropertyDto() })

    fun push(descriptor: DescriptorDto) {
        ruleLists.forEach { (kProperty, validation) ->
            validation.push(
                descriptor.properties.firstOrNull { it.name == kProperty.name }
                    ?: throw IllegalStateException("property ${kProperty.name} missing from the descriptor")
            )
        }
    }
}

interface ValidationRuleList<T> {
    fun validate(report: ValidityReport)
    fun isOptional(): Boolean
    fun setDefault()
    fun push(dto: PropertyDto)
    fun toPropertyDto(): PropertyDto?
}

interface ValidationRule<T> {
    fun validate(value: T, report: ValidityReport)
    fun toValidationDto(): ValidationDto
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