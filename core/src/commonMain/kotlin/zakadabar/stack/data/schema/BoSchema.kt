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

    @Suppress("UNCHECKED_CAST")
    inline operator fun <reified T : Any> KMutableProperty0<out EntityId<T>>.unaryPlus() =
        EntityIdBoSchemaEntry(T::class, this as KMutableProperty0<EntityId<T>>).also { entries[this] = it }

    @Suppress("UNCHECKED_CAST")
    inline operator fun <reified T : Any> KMutableProperty0<out EntityId<T>?>.unaryPlus(): OptEntityIdBoSchemaEntry<*> =
        OptEntityIdBoSchemaEntry(T::class, this as KMutableProperty0<EntityId<T>?>).also { entries[this] = it }

    operator fun KMutableProperty0<String>.unaryPlus() = StringBoSchemaEntry(this).also { entries[this] = it }

    operator fun KMutableProperty0<String?>.unaryPlus() = OptStringBoSchemaEntry(this).also { entries[this] = it }

    operator fun KMutableProperty0<Boolean>.unaryPlus() = BooleanBoSchemaEntry(this).also { entries[this] = it }

    operator fun KMutableProperty0<Boolean?>.unaryPlus() = OptBooleanBoSchemaEntry(this).also { entries[this] = it }

    operator fun KMutableProperty0<Int>.unaryPlus() = IntBoSchemaEntry(this).also { entries[this] = it }

    operator fun KMutableProperty0<Int?>.unaryPlus() = OptIntBoSchemaEntry(this).also { entries[this] = it }

    operator fun KMutableProperty0<Long>.unaryPlus() = LongBoSchemaEntry(this).also { entries[this] = it }

    operator fun KMutableProperty0<Long?>.unaryPlus() = OptLongBoSchemaEntry(this).also { entries[this] = it }

    operator fun KMutableProperty0<Double>.unaryPlus() = DoubleBoSchemaEntry(this).also { entries[this] = it }

    operator fun KMutableProperty0<Double?>.unaryPlus() = OptDoubleBoSchemaEntry(this).also { entries[this] = it }

    operator fun KMutableProperty0<Instant>.unaryPlus() = InstantBoSchemaEntry(this).also { entries[this] = it }

    operator fun KMutableProperty0<Instant?>.unaryPlus() = OptInstantBoSchemaEntry(this).also { entries[this] = it }

    operator fun KMutableProperty0<Secret>.unaryPlus() = SecretBoSchemaEntry(this).also { entries[this] = it }

    operator fun KMutableProperty0<Secret?>.unaryPlus() = OptSecretBoSchemaEntry(this).also { entries[this] = it }

    operator fun KMutableProperty0<UUID>.unaryPlus() = UuidBoSchemaEntry(this).also { entries[this] = it }

    operator fun KMutableProperty0<UUID?>.unaryPlus() = OptUuidBoSchemaEntry(this).also { entries[this] = it }

    inline operator fun <reified T : BaseBo> KMutableProperty0<T>.unaryPlus() = BaseBoBoSchemaEntry(this).also { entries[this] = it }

    inline operator fun <reified T : BaseBo> KMutableProperty0<List<T>>.unaryPlus() = ListBoSchemaEntry(this, T::class).also { entries[this] = it }

    @JsName("SchemaOptEnumUnaryPlus")
    inline operator fun <reified E : Enum<E>> KMutableProperty0<E>.unaryPlus() = EnumBoSchemaEntry(this, enumValues()).also { entries[this] = it }

    inline operator fun <reified E : Enum<E>> KMutableProperty0<E?>.unaryPlus() = OptEnumBoSchemaEntry(this, enumValues()).also { entries[this] = it }

    operator fun plusAssign(custom: CustomBoSchemaEntry) {
        customEntries += custom
    }

    operator fun CustomBoSchemaEntry.unaryPlus() {
        customEntries += this
    }

    @Suppress("DeprecatedCallableAddReplaceWith")
    @Deprecated("EOL: 2021.8.1  -  use function without the rule parameter")
    fun custom(function: (report: ValidityReport, rule: BoPropertyConstraintImpl<Unit>) -> Unit) = CustomBoSchemaEntry(function)

    fun custom(constraintName : String, function: (constraintName : String, report: ValidityReport) -> Unit) =
        CustomBoSchemaEntry(constraintName, function)

    /**
     * Validates the BO that belogs to this schema.
     *
     * @param   allowEmptyId  Allow the "id" property (if exists and is an EntityId) to be empty.
     *                        Used when creating entities.
     *
     * @return  a validity report that contains all failed constraints
     */
    @PublicApi
    fun validate(allowEmptyId: Boolean = false): ValidityReport {
        val report = ValidityReport(allowEmptyId)
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

    /**
     * Get a constraints of a property.
     *
     * @param  propName  name of the property
     *
     * @return list of constraints, empty when there are no constraints
     *
     * @throws NoSuchElementException  when there is no property with the given name
     */
    fun constraints(propName: String): List<BoConstraint> {
        entries.forEach { entry ->
            if (entry.key.name == propName) {
                return entry.value.constraints()
            }
        }
        throw NoSuchElementException()
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
    fun constraints(): List<BoConstraint>
}

interface BoPropertyConstraintImpl<T> {
    fun validate(value: T, report: ValidityReport)
    fun toBoConstraint(): BoConstraint
}