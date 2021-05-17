/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.data.schema

import kotlinx.datetime.Instant
import zakadabar.stack.data.DtoBase
import zakadabar.stack.data.builtin.misc.Secret
import zakadabar.stack.data.record.RecordId
import zakadabar.stack.data.schema.descriptor.ConstraintDto
import zakadabar.stack.data.schema.descriptor.DescriptorDto
import zakadabar.stack.data.schema.descriptor.PropertyDto
import zakadabar.stack.data.schema.entries.*
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
    val entries = mutableMapOf<KMutableProperty0<*>, DtoSchemaEntry<*>>()

    @Suppress("MemberVisibilityCanBePrivate") // To make extensions possible
    val customEntries = mutableListOf<CustomDtoSchemaEntry>()

    inline operator fun <reified T : Any> KMutableProperty0<out RecordId<T>>.unaryPlus(): RecordIdDtoSchemaEntry<*> {
        @Suppress("UNCHECKED_CAST") // at this point out doesn't really matters
        val ruleList = RecordIdDtoSchemaEntry(T::class, this as KMutableProperty0<RecordId<T>>)
        entries[this] = ruleList
        return ruleList
    }

    inline operator fun <reified T : Any> KMutableProperty0<out RecordId<T>?>.unaryPlus(): OptRecordIdDtoSchemaEntry<*> {
        @Suppress("UNCHECKED_CAST") // at this point out doesn't really matters
        val ruleList = OptRecordIdDtoSchemaEntry(T::class, this as KMutableProperty0<RecordId<T>?>)
        entries[this] = ruleList
        return ruleList
    }

    operator fun KMutableProperty0<String>.unaryPlus(): StringDtoSchemaEntry {
        val ruleList = StringDtoSchemaEntry(this)
        entries[this] = ruleList
        return ruleList
    }

    operator fun KMutableProperty0<String?>.unaryPlus(): OptStringDtoSchemaEntry {
        val ruleList = OptStringDtoSchemaEntry(this)
        entries[this] = ruleList
        return ruleList
    }

    operator fun KMutableProperty0<Boolean>.unaryPlus(): BooleanDtoSchemaEntry {
        val ruleList = BooleanDtoSchemaEntry(this)
        entries[this] = ruleList
        return ruleList
    }

    operator fun KMutableProperty0<Boolean?>.unaryPlus(): OptBooleanDtoSchemaEntry {
        val ruleList = OptBooleanDtoSchemaEntry(this)
        entries[this] = ruleList
        return ruleList
    }

    operator fun KMutableProperty0<Int>.unaryPlus(): IntDtoSchemaEntry {
        val ruleList = IntDtoSchemaEntry(this)
        entries[this] = ruleList
        return ruleList
    }

    operator fun KMutableProperty0<Int?>.unaryPlus(): OptIntDtoSchemaEntry {
        val ruleList = OptIntDtoSchemaEntry(this)
        entries[this] = ruleList
        return ruleList
    }

    operator fun KMutableProperty0<Long>.unaryPlus(): LongDtoSchemaEntry {
        val ruleList = LongDtoSchemaEntry(this)
        entries[this] = ruleList
        return ruleList
    }

    operator fun KMutableProperty0<Long?>.unaryPlus(): OptLongDtoSchemaEntry {
        val ruleList = OptLongDtoSchemaEntry(this)
        entries[this] = ruleList
        return ruleList
    }

    operator fun KMutableProperty0<Double>.unaryPlus(): DoubleDtoSchemaEntry {
        val ruleList = DoubleDtoSchemaEntry(this)
        entries[this] = ruleList
        return ruleList
    }

    operator fun KMutableProperty0<Double?>.unaryPlus(): OptDoubleDtoSchemaEntry {
        val ruleList = OptDoubleDtoSchemaEntry(this)
        entries[this] = ruleList
        return ruleList
    }

    operator fun KMutableProperty0<Instant>.unaryPlus(): InstantDtoSchemaEntry {
        val ruleList = InstantDtoSchemaEntry(this)
        entries[this] = ruleList
        return ruleList
    }

    operator fun KMutableProperty0<Instant?>.unaryPlus(): OptInstantDtoSchemaEntry {
        val ruleList = OptInstantDtoSchemaEntry(this)
        entries[this] = ruleList
        return ruleList
    }

    operator fun KMutableProperty0<Secret>.unaryPlus(): SecretDtoSchemaEntry {
        val ruleList = SecretDtoSchemaEntry(this)
        entries[this] = ruleList
        return ruleList
    }

    operator fun KMutableProperty0<Secret?>.unaryPlus(): OptSecretDtoSchemaEntry {
        val ruleList = OptSecretDtoSchemaEntry(this)
        entries[this] = ruleList
        return ruleList
    }

    operator fun KMutableProperty0<UUID>.unaryPlus(): UuidDtoSchemaEntry {
        val ruleList = UuidDtoSchemaEntry(this)
        entries[this] = ruleList
        return ruleList
    }

    operator fun KMutableProperty0<UUID?>.unaryPlus(): OptUuidDtoSchemaEntry {
        val ruleList = OptUuidDtoSchemaEntry(this)
        entries[this] = ruleList
        return ruleList
    }

    inline operator fun <reified T : DtoBase> KMutableProperty0<T>.unaryPlus(): DtoBaseDtoSchemaEntry<T> {
        val ruleList = DtoBaseDtoSchemaEntry(this)
        entries[this] = ruleList
        return ruleList
    }

    @JsName("SchemaOptEnumUnaryPlus")
    inline operator fun <reified E : Enum<E>> KMutableProperty0<E>.unaryPlus(): EnumDtoSchemaEntry<E> {
        val ruleList = EnumDtoSchemaEntry(this, enumValues())
        entries[this] = ruleList
        return ruleList
    }

    inline operator fun <reified E : Enum<E>> KMutableProperty0<E?>.unaryPlus(): OptEnumDtoSchemaEntry<E> {
        val ruleList = OptEnumDtoSchemaEntry(this, enumValues())
        entries[this] = ruleList
        return ruleList
    }

    operator fun plusAssign(custom: CustomDtoSchemaEntry) {
        customEntries += custom
    }

    operator fun CustomDtoSchemaEntry.unaryPlus() {
        customEntries += this
    }

    fun custom(function: (report: ValidityReport, rule: DtoPropertyConstraint<Unit>) -> Unit) = CustomDtoSchemaEntry(function)

    @PublicApi
    fun validate(): ValidityReport {
        val report = ValidityReport()
        entries.forEach { it.value.validate(report) }
        return report
    }

    fun setDefaults() {
        entries.forEach { it.value.setDefault() }
    }

    fun isOptional(propName: String): Boolean {
        entries.forEach {
            if (it.key.name == propName) {
                return it.value.isOptional()
            }
        }
        return true
    }

    // FIXME package and class for DescriptorDto
    fun toDescriptorDto() = DescriptorDto("", "", "", entries.mapNotNull { it.value.toPropertyDto() })

    fun push(descriptor: DescriptorDto) {
        entries.forEach { (kProperty, validation) ->
            validation.push(
                descriptor.properties.firstOrNull { it.name == kProperty.name }
                    ?: throw IllegalStateException("property ${kProperty.name} missing from the descriptor")
            )
        }
    }
}

interface DtoSchemaEntry<T> {
    fun validate(report: ValidityReport)
    fun isOptional(): Boolean
    fun setDefault()
    fun push(dto: PropertyDto)
    fun toPropertyDto(): PropertyDto?
}

interface DtoPropertyConstraint<T> {
    fun validate(value: T, report: ValidityReport)
    fun toValidationDto(): ConstraintDto
}

class ValidityReport(
    @PublicApi
    val fails: MutableMap<String, MutableList<DtoPropertyConstraint<*>>> = mutableMapOf()
) {
    fun fail(property: KProperty0<*>, validation: DtoPropertyConstraint<*>) {
        fails.getOrPut(property.name) { mutableListOf() } += validation
    }

    @PublicApi
    fun dump(): String {
        val lines = mutableListOf<String>()
        fails.forEach { lines += "${it.key} : ${it.value.map { v -> v::class.simpleName }.joinToString(", ")}" }
        return lines.joinToString("\n")
    }
}