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
package zakadabar.stack.data.schema.validations

import zakadabar.stack.data.schema.DtoPropertyConstraint
import zakadabar.stack.data.schema.DtoSchemaEntry
import zakadabar.stack.data.schema.ValidityReport
import zakadabar.stack.data.schema.descriptor.ConstraintDoubleDto
import zakadabar.stack.data.schema.descriptor.ConstraintType
import zakadabar.stack.data.schema.descriptor.DoublePropertyDto
import zakadabar.stack.data.schema.descriptor.PropertyDto
import zakadabar.stack.util.PublicApi
import kotlin.reflect.KMutableProperty0

class DoubleDtoSchemaEntry(val kProperty: KMutableProperty0<Double>) : DtoSchemaEntry<Double> {

    var defaultValue = 0.0

    private val rules = mutableListOf<DtoPropertyConstraint<Double>>()

    inner class Max(@PublicApi val limit: Double) : DtoPropertyConstraint<Double> {

        override fun validate(value: Double, report: ValidityReport) {
            if (value > limit) report.fail(kProperty, this)
        }

        override fun toValidationDto() = ConstraintDoubleDto(ConstraintType.Max, limit)
    }

    inner class Min(@PublicApi val limit: Double) : DtoPropertyConstraint<Double> {
        override fun validate(value: Double, report: ValidityReport) {
            if (value < limit) report.fail(kProperty, this)
        }

        override fun toValidationDto() = ConstraintDoubleDto(ConstraintType.Min, limit)
    }

    inner class NotEquals(@PublicApi val invalidValue: Double) : DtoPropertyConstraint<Double> {
        override fun validate(value: Double, report: ValidityReport) {
            if (value == invalidValue) report.fail(kProperty, this)
        }

        override fun toValidationDto() = ConstraintDoubleDto(ConstraintType.NotEquals, invalidValue)
    }

    @PublicApi
    infix fun max(limit: Double): DoubleDtoSchemaEntry {
        rules += Max(limit)
        return this
    }

    @PublicApi
    infix fun min(limit: Double): DoubleDtoSchemaEntry {
        rules += Min(limit)
        return this
    }

    @PublicApi
    infix fun notEquals(invalidValue: Double): DoubleDtoSchemaEntry {
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
    infix fun default(value: Double): DoubleDtoSchemaEntry {
        defaultValue = value
        return this
    }

    override fun setDefault() {
        kProperty.set(defaultValue)
    }

    override fun isOptional() = false

    override fun push(dto: PropertyDto) {
        require(dto is DoublePropertyDto)
        kProperty.set(dto.value!!)
    }


    override fun toPropertyDto() = DoublePropertyDto(
        kProperty.name,
        isOptional(),
        rules.map { it.toValidationDto() },
        defaultValue,
        kProperty.get()
    )


}