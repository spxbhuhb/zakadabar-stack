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

import zakadabar.stack.data.schema.ValidationRule
import zakadabar.stack.data.schema.ValidationRuleList
import zakadabar.stack.data.schema.ValidityReport
import zakadabar.stack.data.schema.descriptor.DoublePropertyDto
import zakadabar.stack.data.schema.descriptor.DoubleValidationDto
import zakadabar.stack.data.schema.descriptor.PropertyDto
import zakadabar.stack.data.schema.descriptor.ValidationType
import zakadabar.stack.util.PublicApi
import kotlin.reflect.KMutableProperty0

class DoubleValidationRuleList(val kProperty: KMutableProperty0<Double>) : ValidationRuleList<Double> {

    var defaultValue = 0.0

    private val rules = mutableListOf<ValidationRule<Double>>()

    inner class Max(@PublicApi val limit: Double) : ValidationRule<Double> {

        override fun validate(value: Double, report: ValidityReport) {
            if (value > limit) report.fail(kProperty, this)
        }

        override fun toValidationDto() = DoubleValidationDto(ValidationType.Max, limit)
    }

    inner class Min(@PublicApi val limit: Double) : ValidationRule<Double> {
        override fun validate(value: Double, report: ValidityReport) {
            if (value < limit) report.fail(kProperty, this)
        }

        override fun toValidationDto() = DoubleValidationDto(ValidationType.Min, limit)
    }

    inner class NotEquals(@PublicApi val invalidValue: Double) : ValidationRule<Double> {
        override fun validate(value: Double, report: ValidityReport) {
            if (value == invalidValue) report.fail(kProperty, this)
        }

        override fun toValidationDto() = DoubleValidationDto(ValidationType.NotEquals, invalidValue)
    }

    @PublicApi
    infix fun max(limit: Double): DoubleValidationRuleList {
        rules += Max(limit)
        return this
    }

    @PublicApi
    infix fun min(limit: Double): DoubleValidationRuleList {
        rules += Min(limit)
        return this
    }

    @PublicApi
    infix fun notEquals(invalidValue: Double): DoubleValidationRuleList {
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
    infix fun default(value: Double): DoubleValidationRuleList {
        defaultValue = value
        return this
    }

    override fun setDefault() {
        kProperty.set(defaultValue)
    }

    override fun isOptional() = false

    override fun push(dto: PropertyDto) {
        require(dto is DoublePropertyDto)
        kProperty.set(dto.value)
    }


    override fun toPropertyDto() = DoublePropertyDto(
        kProperty.name,
        rules.map { it.toValidationDto() },
        defaultValue,
        kProperty.get()
    )


}