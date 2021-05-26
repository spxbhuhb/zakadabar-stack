/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.data.schema

import kotlinx.datetime.Instant
import zakadabar.stack.data.BaseBo
import zakadabar.stack.data.builtin.misc.Secret
import zakadabar.stack.data.entity.EntityId
import zakadabar.stack.data.schema.descriptor.BoConstraint
import zakadabar.stack.data.schema.descriptor.BoDescriptor
import zakadabar.stack.data.schema.descriptor.BoProperty
import zakadabar.stack.data.schema.entries.*
import zakadabar.stack.util.PublicApi
import zakadabar.stack.util.UUID
import kotlin.js.JsName
import kotlin.reflect.KMutableProperty0
import kotlin.reflect.KProperty0

/**
 * A data model schema that may be used to validate a business object.
 */
open class BoSchema() {

    companion object {
        val NO_VALIDATION = BoSchema()
    }

    constructor(init: BoSchema.() -> Unit) : this() {
        this.init()
    }

    @Suppress("MemberVisibilityCanBePrivate") // To make extensions possible
    val entries = mutableMapOf<KMutableProperty0<*>, BoSchemaEntry<*>>()

    @Suppress("MemberVisibilityCanBePrivate") // To make extensions possible
    val customEntries = mutableListOf<CustomBoSchemaEntry>()

    inline operator fun <reified T : Any> KMutableProperty0<out EntityId<T>>.unaryPlus(): EntityIdBoSchemaEntry<*> {
        @Suppress("UNCHECKED_CAST") // at this point out doesn't really matters
        val ruleList = EntityIdBoSchemaEntry(T::class, this as KMutableProperty0<EntityId<T>>)
        entries[this] = ruleList
        return ruleList
    }

    inline operator fun <reified T : Any> KMutableProperty0<out EntityId<T>?>.unaryPlus(): OptEntityIdBoSchemaEntry<*> {
        @Suppress("UNCHECKED_CAST") // at this point out doesn't really matters
        val ruleList = OptEntityIdBoSchemaEntry(T::class, this as KMutableProperty0<EntityId<T>?>)
        entries[this] = ruleList
        return ruleList
    }

    operator fun KMutableProperty0<String>.unaryPlus(): StringBoSchemaEntry {
        val ruleList = StringBoSchemaEntry(this)
        entries[this] = ruleList
        return ruleList
    }

    operator fun KMutableProperty0<String?>.unaryPlus(): OptStringBoSchemaEntry {
        val ruleList = OptStringBoSchemaEntry(this)
        entries[this] = ruleList
        return ruleList
    }

    operator fun KMutableProperty0<Boolean>.unaryPlus(): BooleanBoSchemaEntry {
        val ruleList = BooleanBoSchemaEntry(this)
        entries[this] = ruleList
        return ruleList
    }

    operator fun KMutableProperty0<Boolean?>.unaryPlus(): OptBooleanBoSchemaEntry {
        val ruleList = OptBooleanBoSchemaEntry(this)
        entries[this] = ruleList
        return ruleList
    }

    operator fun KMutableProperty0<Int>.unaryPlus(): IntBoSchemaEntry {
        val ruleList = IntBoSchemaEntry(this)
        entries[this] = ruleList
        return ruleList
    }

    operator fun KMutableProperty0<Int?>.unaryPlus(): OptIntBoSchemaEntry {
        val ruleList = OptIntBoSchemaEntry(this)
        entries[this] = ruleList
        return ruleList
    }

    operator fun KMutableProperty0<Long>.unaryPlus(): LongBoSchemaEntry {
        val ruleList = LongBoSchemaEntry(this)
        entries[this] = ruleList
        return ruleList
    }

    operator fun KMutableProperty0<Long?>.unaryPlus(): OptLongBoSchemaEntry {
        val ruleList = OptLongBoSchemaEntry(this)
        entries[this] = ruleList
        return ruleList
    }

    operator fun KMutableProperty0<Double>.unaryPlus(): DoubleBoSchemaEntry {
        val ruleList = DoubleBoSchemaEntry(this)
        entries[this] = ruleList
        return ruleList
    }

    operator fun KMutableProperty0<Double?>.unaryPlus(): OptDoubleBoSchemaEntry {
        val ruleList = OptDoubleBoSchemaEntry(this)
        entries[this] = ruleList
        return ruleList
    }

    operator fun KMutableProperty0<Instant>.unaryPlus(): InstantBoSchemaEntry {
        val ruleList = InstantBoSchemaEntry(this)
        entries[this] = ruleList
        return ruleList
    }

    operator fun KMutableProperty0<Instant?>.unaryPlus(): OptInstantBoSchemaEntry {
        val ruleList = OptInstantBoSchemaEntry(this)
        entries[this] = ruleList
        return ruleList
    }

    operator fun KMutableProperty0<Secret>.unaryPlus(): SecretBoSchemaEntry {
        val ruleList = SecretBoSchemaEntry(this)
        entries[this] = ruleList
        return ruleList
    }

    operator fun KMutableProperty0<Secret?>.unaryPlus(): OptSecretBoSchemaEntry {
        val ruleList = OptSecretBoSchemaEntry(this)
        entries[this] = ruleList
        return ruleList
    }

    operator fun KMutableProperty0<UUID>.unaryPlus(): UuidBoSchemaEntry {
        val ruleList = UuidBoSchemaEntry(this)
        entries[this] = ruleList
        return ruleList
    }

    operator fun KMutableProperty0<UUID?>.unaryPlus(): OptUuidBoSchemaEntry {
        val ruleList = OptUuidBoSchemaEntry(this)
        entries[this] = ruleList
        return ruleList
    }

    inline operator fun <reified T : BaseBo> KMutableProperty0<T>.unaryPlus(): BaseBoBoSchemaEntry<T> {
        val ruleList = BaseBoBoSchemaEntry(this)
        entries[this] = ruleList
        return ruleList
    }

    @JsName("SchemaOptEnumUnaryPlus")
    inline operator fun <reified E : Enum<E>> KMutableProperty0<E>.unaryPlus(): EnumBoSchemaEntry<E> {
        val ruleList = EnumBoSchemaEntry(this, enumValues())
        entries[this] = ruleList
        return ruleList
    }

    inline operator fun <reified E : Enum<E>> KMutableProperty0<E?>.unaryPlus(): OptEnumBoSchemaEntry<E> {
        val ruleList = OptEnumBoSchemaEntry(this, enumValues())
        entries[this] = ruleList
        return ruleList
    }

    operator fun plusAssign(custom: CustomBoSchemaEntry) {
        customEntries += custom
    }

    operator fun CustomBoSchemaEntry.unaryPlus() {
        customEntries += this
    }

    fun custom(function: (report: ValidityReport, rule: BoPropertyConstraintImpl<Unit>) -> Unit) = CustomBoSchemaEntry(function)

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

    // FIXME package and class for BoDescriptor
    fun toBoDescriptor() = BoDescriptor("", "", "", entries.mapNotNull { it.value.toBoProperty() })

    fun push(boDescriptor: BoDescriptor) {
        entries.forEach { (kProperty, validation) ->
            validation.push(
                boDescriptor.properties.firstOrNull { it.name == kProperty.name }
                    ?: throw IllegalStateException("property ${kProperty.name} missing from the descriptor")
            )
        }
    }
}

interface BoSchemaEntry<T> {
    fun validate(report: ValidityReport)
    fun isOptional(): Boolean
    fun setDefault()
    fun push(bo: BoProperty)
    fun toBoProperty(): BoProperty?
}

interface BoPropertyConstraintImpl<T> {
    fun validate(value: T, report: ValidityReport)
    fun toBoConstraint(): BoConstraint
}

class ValidityReport(
    @PublicApi
    val fails: MutableMap<String, MutableList<BoPropertyConstraintImpl<*>>> = mutableMapOf()
) {
    fun fail(property: KProperty0<*>, validation: BoPropertyConstraintImpl<*>) {
        fails.getOrPut(property.name) { mutableListOf() } += validation
    }

    @PublicApi
    fun dump(): String {
        val lines = mutableListOf<String>()
        fails.forEach { lines += "${it.key} : ${it.value.map { v -> v::class.simpleName }.joinToString(", ")}" }
        return lines.joinToString("\n")
    }
}