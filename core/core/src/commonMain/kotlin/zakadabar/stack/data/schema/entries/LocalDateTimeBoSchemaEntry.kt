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

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import zakadabar.stack.data.schema.BoPropertyConstraintImpl
import zakadabar.stack.data.schema.BoSchemaEntry
import zakadabar.stack.data.schema.ValidityReport
import zakadabar.stack.data.schema.descriptor.BoConstraintType
import zakadabar.stack.data.schema.descriptor.BoProperty
import zakadabar.stack.data.schema.descriptor.LocalDateTimeBoConstraint
import zakadabar.stack.data.schema.descriptor.LocalDateTimeBoProperty
import zakadabar.stack.util.PublicApi
import kotlin.reflect.KMutableProperty0

class LocalDateTimeBoSchemaEntry(val kProperty: KMutableProperty0<LocalDateTime>) : BoSchemaEntry<LocalDateTime> {

    var defaultValue: LocalDateTime? = null

    private val rules = mutableListOf<BoPropertyConstraintImpl<LocalDateTime>>()

    inner class Max(@PublicApi val limit: LocalDateTime) : BoPropertyConstraintImpl<LocalDateTime> {

        override fun validate(value: LocalDateTime, report: ValidityReport) {
            if (value > limit) report.fail(kProperty, this)
        }

        override fun toBoConstraint() = LocalDateTimeBoConstraint(BoConstraintType.Max, limit)

    }

    inner class Min(@PublicApi val limit: LocalDateTime) : BoPropertyConstraintImpl<LocalDateTime> {

        override fun validate(value: LocalDateTime, report: ValidityReport) {
            if (value < limit) report.fail(kProperty, this)
        }

        override fun toBoConstraint() = LocalDateTimeBoConstraint(BoConstraintType.Min, limit)

    }

    inner class Before(@PublicApi val limit: LocalDateTime) : BoPropertyConstraintImpl<LocalDateTime> {

        override fun validate(value: LocalDateTime, report: ValidityReport) {
            if (value >= limit) report.fail(kProperty, this)
        }

        override fun toBoConstraint() = LocalDateTimeBoConstraint(BoConstraintType.Before, limit)

    }

    inner class After(@PublicApi val limit: LocalDateTime) : BoPropertyConstraintImpl<LocalDateTime> {

        override fun validate(value: LocalDateTime, report: ValidityReport) {
            if (value <= limit) report.fail(kProperty, this)
        }

        override fun toBoConstraint() = LocalDateTimeBoConstraint(BoConstraintType.After, limit)

    }

    inner class NotEquals(@PublicApi val invalidValue: LocalDateTime) : BoPropertyConstraintImpl<LocalDateTime> {

        override fun validate(value: LocalDateTime, report: ValidityReport) {
            if (value == invalidValue) report.fail(kProperty, this)
        }

        override fun toBoConstraint() = LocalDateTimeBoConstraint(BoConstraintType.NotEquals, invalidValue)

    }

    @PublicApi
    infix fun max(limit: LocalDateTime): LocalDateTimeBoSchemaEntry {
        rules += Max(limit)
        return this
    }

    @PublicApi
    infix fun min(limit: LocalDateTime): LocalDateTimeBoSchemaEntry {
        rules += Min(limit)
        return this
    }

    @PublicApi
    infix fun before(limit: LocalDateTime): LocalDateTimeBoSchemaEntry {
        rules += Before(limit)
        return this
    }

    @PublicApi
    infix fun after(limit: LocalDateTime): LocalDateTimeBoSchemaEntry {
        rules += After(limit)
        return this
    }

    @PublicApi
    infix fun notEquals(invalidValue: LocalDateTime): LocalDateTimeBoSchemaEntry {
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
    infix fun default(value: LocalDateTime): LocalDateTimeBoSchemaEntry {
        defaultValue = value
        return this
    }

    override fun setDefault() {
        kProperty.set(defaultValue ?: Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()))
    }

    override fun decodeFromText(text : String?) : LocalDateTime {
        return LocalDateTime.parse(text!!)
    }

    override fun setFromText(text: String?) {
        kProperty.set(decodeFromText(text))
    }

    override fun isOptional() = false

    override fun push(bo: BoProperty) {
        require(bo is LocalDateTimeBoProperty)
        kProperty.set(bo.value !!)
    }

    override fun toBoProperty() = LocalDateTimeBoProperty(
        kProperty.name,
        isOptional(),
        constraints(),
        defaultValue,
        kProperty.get()
    )

    override fun constraints() = rules.map { it.toBoConstraint() }

}