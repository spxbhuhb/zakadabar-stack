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

import kotlinx.datetime.Instant
import zakadabar.stack.data.schema.DtoPropertyConstraint
import zakadabar.stack.data.schema.DtoSchemaEntry
import zakadabar.stack.data.schema.ValidityReport
import zakadabar.stack.data.schema.descriptor.ConstraintInstantDto
import zakadabar.stack.data.schema.descriptor.ConstraintType
import zakadabar.stack.data.schema.descriptor.InstantPropertyDto
import zakadabar.stack.data.schema.descriptor.PropertyDto
import zakadabar.stack.util.PublicApi
import kotlin.reflect.KMutableProperty0

class OptInstantDtoSchemaEntry(val kProperty: KMutableProperty0<Instant?>) : DtoSchemaEntry<Instant?> {

    var defaultValue: Instant? = null

    private val rules = mutableListOf<DtoPropertyConstraint<Instant?>>()

    inner class Max(@PublicApi val limit: Instant) : DtoPropertyConstraint<Instant?> {

        override fun validate(value: Instant?, report: ValidityReport) {
            if (value != null && value > limit) report.fail(kProperty, this)
        }

        override fun toValidationDto() = ConstraintInstantDto(ConstraintType.Max, limit)

    }

    inner class Min(@PublicApi val limit: Instant) : DtoPropertyConstraint<Instant?> {

        override fun validate(value: Instant?, report: ValidityReport) {
            if (value != null && value < limit) report.fail(kProperty, this)
        }

        override fun toValidationDto() = ConstraintInstantDto(ConstraintType.Min, limit)

    }

    inner class Before(@PublicApi val limit: Instant) : DtoPropertyConstraint<Instant?> {

        override fun validate(value: Instant?, report: ValidityReport) {
            if (value != null && value >= limit) report.fail(kProperty, this)
        }

        override fun toValidationDto() = ConstraintInstantDto(ConstraintType.Before, limit)

    }

    inner class After(@PublicApi val limit: Instant) : DtoPropertyConstraint<Instant?> {

        override fun validate(value: Instant?, report: ValidityReport) {
            if (value != null && value <= limit) report.fail(kProperty, this)
        }

        override fun toValidationDto() = ConstraintInstantDto(ConstraintType.After, limit)

    }

    inner class NotEquals(@PublicApi val invalidValue: Instant) : DtoPropertyConstraint<Instant?> {

        override fun validate(value: Instant?, report: ValidityReport) {
            if (value == invalidValue) report.fail(kProperty, this)
        }

        override fun toValidationDto() = ConstraintInstantDto(ConstraintType.NotEquals, invalidValue)

    }

    @PublicApi
    infix fun max(limit: Instant): OptInstantDtoSchemaEntry {
        rules += Max(limit)
        return this
    }

    @PublicApi
    infix fun min(limit: Instant): OptInstantDtoSchemaEntry {
        rules += Min(limit)
        return this
    }

    @PublicApi
    infix fun before(limit: Instant): OptInstantDtoSchemaEntry {
        rules += Before(limit)
        return this
    }

    @PublicApi
    infix fun after(limit: Instant): OptInstantDtoSchemaEntry {
        rules += After(limit)
        return this
    }

    @PublicApi
    infix fun notEquals(invalidValue: Instant): OptInstantDtoSchemaEntry {
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
    infix fun default(value: Instant?): OptInstantDtoSchemaEntry {
        defaultValue = value
        return this
    }

    override fun setDefault() {
        kProperty.set(defaultValue)
    }

    override fun isOptional() = true

    override fun push(dto: PropertyDto) {
        require(dto is InstantPropertyDto)
        kProperty.set(dto.value)
    }

    override fun toPropertyDto() = InstantPropertyDto(
        kProperty.name,
        isOptional(),
        rules.map { it.toValidationDto() },
        defaultValue,
        kProperty.get()
    )

}