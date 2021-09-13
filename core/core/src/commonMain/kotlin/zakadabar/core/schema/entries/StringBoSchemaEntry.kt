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
import zakadabar.core.schema.BoSchemaEntryExtension
import zakadabar.core.schema.ValidityReport
import zakadabar.core.schema.descriptor.*
import zakadabar.core.util.PublicApi
import kotlin.reflect.KMutableProperty0

class StringBoSchemaEntry(
    override val kProperty: KMutableProperty0<String>
    ) : BoSchemaEntry<String, StringBoSchemaEntry> {

    override val rules = mutableListOf<BoPropertyConstraintImpl<String>>()

    override val extensions = mutableListOf<BoSchemaEntryExtension<String>>()

    override var defaultValue = ""

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

    inner class Format(@PublicApi val pattern: String) : BoPropertyConstraintImpl<String> {

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

    override fun decodeFromText(text: String?): String {
        return text !!
    }

    override fun setFromText(text: String?) {
        kProperty.set(decodeFromText(text))
    }

    override fun isOptional() = false

    override fun push(bo: BoProperty) {
        require(bo is StringBoProperty)
        kProperty.set(bo.value !!)
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