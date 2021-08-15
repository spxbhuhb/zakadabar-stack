/*
 * Copyright © 2020, Simplexion, Hungary and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package zakadabar.core.schema.entries

import zakadabar.core.schema.BoPropertyConstraintImpl
import zakadabar.core.schema.BoSchemaEntry
import zakadabar.core.schema.ValidityReport
import zakadabar.core.schema.descriptor.BoConstraintType
import zakadabar.core.schema.descriptor.BoProperty
import zakadabar.core.schema.descriptor.DoubleBoConstraint
import zakadabar.core.schema.descriptor.DoubleBoProperty
import zakadabar.core.util.PublicApi
import kotlin.reflect.KMutableProperty0

class DoubleBoSchemaEntry(val kProperty: KMutableProperty0<Double>) : BoSchemaEntry<Double> {

    var defaultValue = 0.0

    private val rules = mutableListOf<BoPropertyConstraintImpl<Double>>()

    inner class Max(@PublicApi val limit: Double) : BoPropertyConstraintImpl<Double> {

        override fun validate(value: Double, report: ValidityReport) {
            if (value > limit) report.fail(kProperty, this)
        }

        override fun toBoConstraint() = DoubleBoConstraint(BoConstraintType.Max, limit)
    }

    inner class Min(@PublicApi val limit: Double) : BoPropertyConstraintImpl<Double> {
        override fun validate(value: Double, report: ValidityReport) {
            if (value < limit) report.fail(kProperty, this)
        }

        override fun toBoConstraint() = DoubleBoConstraint(BoConstraintType.Min, limit)
    }

    inner class NotEquals(@PublicApi val invalidValue: Double) : BoPropertyConstraintImpl<Double> {
        override fun validate(value: Double, report: ValidityReport) {
            if (value == invalidValue) report.fail(kProperty, this)
        }

        override fun toBoConstraint() = DoubleBoConstraint(BoConstraintType.NotEquals, invalidValue)
    }

    @PublicApi
    infix fun max(limit: Double): DoubleBoSchemaEntry {
        rules += Max(limit)
        return this
    }

    @PublicApi
    infix fun min(limit: Double): DoubleBoSchemaEntry {
        rules += Min(limit)
        return this
    }

    @PublicApi
    infix fun notEquals(invalidValue: Double): DoubleBoSchemaEntry {
        rules += NotEquals(invalidValue)
        return this
    }

    override fun validate(report: ValidityReport) {
        val value = kProperty.get()
        for (rule in rules) {
            rule.validate(value, report)
        }
    }

    @PublicApi
    infix fun default(value: Double): DoubleBoSchemaEntry {
        defaultValue = value
        return this
    }

    override fun setDefault() {
        kProperty.set(defaultValue)
    }

    override fun decodeFromText(text : String?) : Double {
        return text!!.toDouble()
    }

    override fun setFromText(text: String?) {
        kProperty.set(decodeFromText(text))
    }

    override fun isOptional() = false

    override fun push(bo: BoProperty) {
        require(bo is DoubleBoProperty)
        kProperty.set(bo.value !!)
    }

    override fun toBoProperty() = DoubleBoProperty(
        kProperty.name,
        isOptional(),
        constraints(),
        defaultValue,
        kProperty.get()
    )

    override fun constraints() = rules.map { it.toBoConstraint() }

}