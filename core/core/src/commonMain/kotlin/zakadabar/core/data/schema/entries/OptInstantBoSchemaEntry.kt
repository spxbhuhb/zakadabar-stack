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
package zakadabar.core.data.schema.entries

import kotlinx.datetime.Instant
import zakadabar.core.data.schema.BoPropertyConstraintImpl
import zakadabar.core.data.schema.BoSchemaEntry
import zakadabar.core.data.schema.ValidityReport
import zakadabar.core.data.schema.descriptor.BoConstraintType
import zakadabar.core.data.schema.descriptor.BoProperty
import zakadabar.core.data.schema.descriptor.InstantBoConstraint
import zakadabar.core.data.schema.descriptor.InstantBoProperty
import zakadabar.core.util.PublicApi
import kotlin.reflect.KMutableProperty0

class OptInstantBoSchemaEntry(val kProperty: KMutableProperty0<Instant?>) : BoSchemaEntry<Instant?> {

    var defaultValue: Instant? = null

    private val rules = mutableListOf<BoPropertyConstraintImpl<Instant?>>()

    inner class Max(@PublicApi val limit: Instant) : BoPropertyConstraintImpl<Instant?> {

        override fun validate(value: Instant?, report: ValidityReport) {
            if (value != null && value > limit) report.fail(kProperty, this)
        }

        override fun toBoConstraint() = InstantBoConstraint(BoConstraintType.Max, limit)

    }

    inner class Min(@PublicApi val limit: Instant) : BoPropertyConstraintImpl<Instant?> {

        override fun validate(value: Instant?, report: ValidityReport) {
            if (value != null && value < limit) report.fail(kProperty, this)
        }

        override fun toBoConstraint() = InstantBoConstraint(BoConstraintType.Min, limit)

    }

    inner class Before(@PublicApi val limit: Instant) : BoPropertyConstraintImpl<Instant?> {

        override fun validate(value: Instant?, report: ValidityReport) {
            if (value != null && value >= limit) report.fail(kProperty, this)
        }

        override fun toBoConstraint() = InstantBoConstraint(BoConstraintType.Before, limit)

    }

    inner class After(@PublicApi val limit: Instant) : BoPropertyConstraintImpl<Instant?> {

        override fun validate(value: Instant?, report: ValidityReport) {
            if (value != null && value <= limit) report.fail(kProperty, this)
        }

        override fun toBoConstraint() = InstantBoConstraint(BoConstraintType.After, limit)

    }

    inner class NotEquals(@PublicApi val invalidValue: Instant) : BoPropertyConstraintImpl<Instant?> {

        override fun validate(value: Instant?, report: ValidityReport) {
            if (value == invalidValue) report.fail(kProperty, this)
        }

        override fun toBoConstraint() = InstantBoConstraint(BoConstraintType.NotEquals, invalidValue)

    }

    @PublicApi
    infix fun max(limit: Instant): OptInstantBoSchemaEntry {
        rules += Max(limit)
        return this
    }

    @PublicApi
    infix fun min(limit: Instant): OptInstantBoSchemaEntry {
        rules += Min(limit)
        return this
    }

    @PublicApi
    infix fun before(limit: Instant): OptInstantBoSchemaEntry {
        rules += Before(limit)
        return this
    }

    @PublicApi
    infix fun after(limit: Instant): OptInstantBoSchemaEntry {
        rules += After(limit)
        return this
    }

    @PublicApi
    infix fun notEquals(invalidValue: Instant): OptInstantBoSchemaEntry {
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
    infix fun default(value: Instant?): OptInstantBoSchemaEntry {
        defaultValue = value
        return this
    }

    override fun setDefault() {
        kProperty.set(defaultValue)
    }

    override fun decodeFromText(text : String?) : Instant? {
        return text?.let { Instant.parse(text) }
    }

    override fun setFromText(text: String?) {
        kProperty.set(decodeFromText(text))
    }

    override fun isOptional() = true

    override fun push(bo: BoProperty) {
        require(bo is InstantBoProperty)
        kProperty.set(bo.value)
    }

    override fun toBoProperty() = InstantBoProperty(
        kProperty.name,
        isOptional(),
        constraints(),
        defaultValue,
        kProperty.get()
    )

    override fun constraints() = rules.map { it.toBoConstraint() }

}