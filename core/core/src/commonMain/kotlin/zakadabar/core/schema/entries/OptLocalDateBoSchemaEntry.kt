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

import kotlinx.datetime.LocalDate
import zakadabar.core.schema.BoPropertyConstraintImpl
import zakadabar.core.schema.BoSchemaEntry
import zakadabar.core.schema.BoSchemaEntryExtension
import zakadabar.core.schema.ValidityReport
import zakadabar.core.schema.descriptor.BoConstraintType
import zakadabar.core.schema.descriptor.BoProperty
import zakadabar.core.schema.descriptor.LocalDateBoConstraint
import zakadabar.core.schema.descriptor.LocalDateBoProperty
import zakadabar.core.util.PublicApi
import kotlin.reflect.KMutableProperty0

class OptLocalDateBoSchemaEntry(
    override val kProperty: KMutableProperty0<LocalDate?>
) : BoSchemaEntry<LocalDate?, OptLocalDateBoSchemaEntry> {

    override val rules = mutableListOf<BoPropertyConstraintImpl<LocalDate?>>()

    override val extensions = mutableListOf<BoSchemaEntryExtension<LocalDate?>>()

    override var defaultValue: LocalDate? = null

    inner class Max(@PublicApi val limit: LocalDate) : BoPropertyConstraintImpl<LocalDate?> {

        override fun validate(value: LocalDate?, report: ValidityReport) {
            if (value != null && value > limit) report.fail(kProperty, this)
        }

        override fun toBoConstraint() = LocalDateBoConstraint(BoConstraintType.Max, limit)

    }

    inner class Min(@PublicApi val limit: LocalDate) : BoPropertyConstraintImpl<LocalDate?> {

        override fun validate(value: LocalDate?, report: ValidityReport) {
            if (value != null && value < limit) report.fail(kProperty, this)
        }

        override fun toBoConstraint() = LocalDateBoConstraint(BoConstraintType.Min, limit)

    }

    inner class Before(@PublicApi val limit: LocalDate) : BoPropertyConstraintImpl<LocalDate?> {

        override fun validate(value: LocalDate?, report: ValidityReport) {
            if (value != null && value >= limit) report.fail(kProperty, this)
        }

        override fun toBoConstraint() = LocalDateBoConstraint(BoConstraintType.Before, limit)

    }

    inner class After(@PublicApi val limit: LocalDate) : BoPropertyConstraintImpl<LocalDate?> {

        override fun validate(value: LocalDate?, report: ValidityReport) {
            if (value != null && value <= limit) report.fail(kProperty, this)
        }

        override fun toBoConstraint() = LocalDateBoConstraint(BoConstraintType.After, limit)

    }

    inner class NotEquals(@PublicApi val invalidValue: LocalDate?) : BoPropertyConstraintImpl<LocalDate?> {

        override fun validate(value: LocalDate?, report: ValidityReport) {
            if (value == invalidValue) report.fail(kProperty, this)
        }

        override fun toBoConstraint() = LocalDateBoConstraint(BoConstraintType.NotEquals, invalidValue)

    }

    @PublicApi
    infix fun max(limit: LocalDate): OptLocalDateBoSchemaEntry {
        rules += Max(limit)
        return this
    }

    @PublicApi
    infix fun min(limit: LocalDate): OptLocalDateBoSchemaEntry {
        rules += Min(limit)
        return this
    }

    @PublicApi
    infix fun before(limit: LocalDate): OptLocalDateBoSchemaEntry {
        rules += Before(limit)
        return this
    }

    @PublicApi
    infix fun after(limit: LocalDate): OptLocalDateBoSchemaEntry {
        rules += After(limit)
        return this
    }

    @PublicApi
    infix fun notEquals(invalidValue: LocalDate?): OptLocalDateBoSchemaEntry {
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
    infix fun default(value: LocalDate?): OptLocalDateBoSchemaEntry {
        defaultValue = value
        return this
    }

    override fun setDefault() {
        kProperty.set(defaultValue)
    }

    override fun decodeFromText(text: String?): LocalDate? {
        return text?.let { LocalDate.parse(it) }
    }

    override fun setFromText(text: String?) {
        kProperty.set(decodeFromText(text))
    }

    override fun isOptional() = true

    override fun push(bo: BoProperty) {
        require(bo is LocalDateBoProperty)
        kProperty.set(bo.value !!)
    }

    override fun toBoProperty() = LocalDateBoProperty(
        kProperty.name,
        isOptional(),
        constraints(),
        defaultValue,
        kProperty.get()
    )

    override fun constraints() = rules.map { it.toBoConstraint() }

}