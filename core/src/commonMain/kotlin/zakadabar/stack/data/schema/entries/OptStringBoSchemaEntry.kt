/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.data.schema.entries

import zakadabar.stack.data.schema.BoPropertyConstraintImpl
import zakadabar.stack.data.schema.BoSchemaEntry
import zakadabar.stack.data.schema.ValidityReport
import zakadabar.stack.data.schema.descriptor.*
import zakadabar.stack.util.PublicApi
import kotlin.reflect.KMutableProperty0

class OptStringBoSchemaEntry(val kProperty: KMutableProperty0<String?>) : BoSchemaEntry<String?> {

    var defaultValue: String? = null

    private val rules = mutableListOf<BoPropertyConstraintImpl<String?>>()

    inner class Max(@PublicApi val limit: Int) : BoPropertyConstraintImpl<String?> {

        override fun validate(value: String?, report: ValidityReport) {
            if (value != null && value.length > limit) report.fail(kProperty, this)
        }

        override fun toBoConstraint() = IntBoConstraint(BoConstraintType.Max, limit)

    }

    inner class Min(@PublicApi val limit: Int) : BoPropertyConstraintImpl<String?> {

        override fun validate(value: String?, report: ValidityReport) {
            if (value != null && value.length < limit) report.fail(kProperty, this)
        }

        override fun toBoConstraint() = IntBoConstraint(BoConstraintType.Min, limit)

    }

    inner class NotEquals(@PublicApi val invalidValue: String) : BoPropertyConstraintImpl<String?> {

        override fun validate(value: String?, report: ValidityReport) {
            if (value == invalidValue) report.fail(kProperty, this)
        }

        override fun toBoConstraint() = StringBoConstraint(BoConstraintType.NotEquals, invalidValue)

    }


    inner class Blank(@PublicApi val validValue: Boolean) : BoPropertyConstraintImpl<String?> {

        override fun validate(value: String?, report: ValidityReport) {
            if (value?.isBlank() != validValue) report.fail(kProperty, this)
        }

        override fun toBoConstraint() = BooleanBoConstraint(BoConstraintType.Blank, validValue)

    }

    @PublicApi
    infix fun max(limit: Int): OptStringBoSchemaEntry {
        rules += Max(limit)
        return this
    }

    @PublicApi
    infix fun min(limit: Int): OptStringBoSchemaEntry {
        rules += Min(limit)
        return this
    }

    @PublicApi
    infix fun notEquals(invalidValue: String): OptStringBoSchemaEntry {
        rules += NotEquals(invalidValue)
        return this
    }

    @PublicApi
    infix fun blank(blank: Boolean): OptStringBoSchemaEntry {
        rules += Blank(blank)
        return this
    }

    override fun validate(report: ValidityReport) {
        val value = kProperty.get()
        for (rule in rules) {
            rule.validate(value, report)
        }
    }

    @PublicApi
    infix fun default(value: String?): OptStringBoSchemaEntry {
        defaultValue = value
        return this
    }

    override fun setDefault() {
        kProperty.set(defaultValue)
    }

    override fun isOptional() = true

    override fun push(bo: BoProperty) {
        require(bo is StringBoProperty)
        kProperty.set(bo.value)
    }

    override fun toBoProperty() = StringBoProperty(
        kProperty.name,
        isOptional(),
        rules.map { it.toBoConstraint() },
        defaultValue,
        kProperty.get()
    )
}