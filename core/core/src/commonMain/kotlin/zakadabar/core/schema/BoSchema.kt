/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.schema

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import zakadabar.core.data.BaseBo
import zakadabar.core.data.EntityId
import zakadabar.core.data.Secret
import zakadabar.core.schema.descriptor.BoConstraint
import zakadabar.core.schema.descriptor.BoDescriptor
import zakadabar.core.schema.entries.*
import zakadabar.core.util.PublicApi
import zakadabar.core.util.UUID
import zakadabar.core.util.default
import kotlin.js.JsName
import kotlin.reflect.KClass
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
    val entries = mutableMapOf<KMutableProperty0<*>, BoSchemaEntry<*, *>>()

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

    operator fun KMutableProperty0<LocalDate>.unaryPlus() = LocalDateBoSchemaEntry(this).also { entries[this] = it }

    operator fun KMutableProperty0<LocalDate?>.unaryPlus() = OptLocalDateBoSchemaEntry(this).also { entries[this] = it }

    operator fun KMutableProperty0<LocalDateTime>.unaryPlus() = LocalDateTimeBoSchemaEntry(this).also { entries[this] = it }

    operator fun KMutableProperty0<LocalDateTime?>.unaryPlus() = OptLocalDateTimeBoSchemaEntry(this).also { entries[this] = it }

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

    inline operator fun <reified T : BaseBo> KMutableProperty0<T>.unaryPlus() = BaseBoBoSchemaEntry(this, default()).also { entries[this] = it }

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

    fun custom(constraintName: String, function: (constraintName: String, report: ValidityReport) -> Unit) =
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
    fun constraints(propName: String): List<BoConstraint> =
        constraintsOrNull(propName) ?: throw NoSuchElementException()

    /**
     * Get a constraints of a property.
     *
     * @param  propName  name of the property
     *
     * @return list of constraints, empty when there are no constraints, null when there is no such property
     */
    fun constraintsOrNull(propName: String): List<BoConstraint>? {
        entries.forEach { entry ->
            if (entry.key.name == propName) {
                return entry.value.constraints()
            }
        }
        return null
    }

    /**
     * Collect extensions of a given type. This function is recursive, it collects
     * extensions of sub-schemas as well.
     *
     * @param extensionClass The type of extension to collect.
     *
     * @return A list of (schema entry, extension) pairs in which all extensions are
     *         instances of extensionClass.
     */
    fun <ET : BoSchemaEntryExtension<*>> extensions(extensionClass : KClass<ET>): List<Pair<BoSchemaEntry<*, *>, ET>> {
        val result = mutableListOf<Pair<BoSchemaEntry<*, *>, ET>>()

        entries.forEach { entry ->
            val v = entry.value
            v.extensions.filter { extensionClass.isInstance(it) }.forEach {
                @Suppress("UNCHECKED_CAST") // checked in the line above
                result += (v to (it as ET))
            }
            if (v is BaseBoBoSchemaEntry) {
                result += (v.kProperty.get() as BaseBo).schema().extensions(extensionClass)
            }
        }

        return result
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