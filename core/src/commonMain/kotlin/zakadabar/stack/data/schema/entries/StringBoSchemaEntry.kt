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
package zakadabar.stack.data.schema.entries

import zakadabar.stack.data.schema.BoPropertyConstraintImpl
import zakadabar.stack.data.schema.BoSchemaEntry
import zakadabar.stack.data.schema.ValidityReport
import zakadabar.stack.data.schema.descriptor.*
import zakadabar.stack.util.PublicApi
import kotlin.reflect.KMutableProperty0

class StringBoSchemaEntry(val kProperty: KMutableProperty0<String>) : BoSchemaEntry<String> {

    var defaultValue = ""

    private val rules = mutableListOf<BoPropertyConstraintImpl<String>>()

    inner class Max(@PublicApi val limit: Int) : BoPropertyConstraintImpl<String> {

        override fun validate(value: String, report: ValidityReport) {
            if (value.length > limit) report.fail(kProperty, this)
        }

        override fun toBoConstraint() = IntBoConstraint(BoConstraintType.Max, limit)

    }

    inner class Min(@PublicApi val limit: Int) : BoPropertyConstraintImpl<String> {

        override fun validate(value: String, report: ValidityReport) {
            if (value.length < limit) report.fail(kProperty, this)
        }

        override fun toBoConstraint() = IntBoConstraint(BoConstraintType.Min, limit)

    }

    inner class NotEquals(@PublicApi val invalidValue: String) : BoPropertyConstraintImpl<String> {

        override fun validate(value: String, report: ValidityReport) {
            if (value == invalidValue) report.fail(kProperty, this)
        }

        override fun toBoConstraint() = StringBoConstraint(BoConstraintType.NotEquals, invalidValue)

    }

    inner class Blank(@PublicApi val validValue: Boolean) : BoPropertyConstraintImpl<String> {

        override fun validate(value: String, report: ValidityReport) {
            if (value.isBlank() != validValue) report.fail(kProperty, this)
        }

        override fun toBoConstraint() = BooleanBoConstraint(BoConstraintType.Blank, validValue)

    }

    inner class Format(@PublicApi val pattern : String) : BoPropertyConstraintImpl<String> {

        override fun validate(value: String, report: ValidityReport) {
            if (Regex(pattern).matchEntire(value) == null) report.fail(kProperty, this)
        }

        override fun toBoConstraint() = StringBoConstraint(BoConstraintType.Format, pattern)
    }

    @PublicApi
    infix fun max(limit: Int): StringBoSchemaEntry {
        rules += Max(limit)
        return this
    }

    @PublicApi
    infix fun min(limit: Int): StringBoSchemaEntry {
        rules += Min(limit)
        return this
    }

    @PublicApi
    infix fun notEquals(invalidValue: String): StringBoSchemaEntry {
        rules += NotEquals(invalidValue)
        return this
    }

    @PublicApi
    infix fun blank(blank: Boolean): StringBoSchemaEntry {
        rules += Blank(blank)
        return this
    }

    @PublicApi
    infix fun format(pattern: String): StringBoSchemaEntry {
        rules += Format(pattern)
        return this
    }

    override fun validate(report: ValidityReport) {
        val value = kProperty.get()
        for (rule in rules) {
            rule.validate(value, report)
        }
    }

    @PublicApi
    infix fun default(value: String): StringBoSchemaEntry {
        defaultValue = value
        return this
    }

    override fun setDefault() {
        kProperty.set(defaultValue)
    }

    override fun isOptional() = false

    override fun push(bo: BoProperty) {
        require(bo is StringBoProperty)
        kProperty.set(bo.value!!)
    }

    override fun toBoProperty() = StringBoProperty(
        kProperty.name,
        isOptional(),
        constraints(),
        defaultValue,
        kProperty.get()
    )

    override fun constraints() = rules.map { it.toBoConstraint() }

}