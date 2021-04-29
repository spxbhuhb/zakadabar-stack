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

import kotlinx.datetime.Instant
import zakadabar.stack.data.schema.ValidationRule
import zakadabar.stack.data.schema.ValidationRuleList
import zakadabar.stack.data.schema.ValidityReport
import zakadabar.stack.data.schema.dto.*
import zakadabar.stack.util.PublicApi
import kotlin.reflect.KMutableProperty0

class OptInstantValidationRuleList(val kProperty: KMutableProperty0<Instant?>) : ValidationRuleList<Instant?> {

    var defaultValue: Instant? = null

    private val rules = mutableListOf<ValidationRule<Instant?>>()

    inner class Max(@PublicApi val limit: Instant) : ValidationRule<Instant?> {

        override fun validate(value: Instant?, report: ValidityReport) {
            if (value != null && value > limit) report.fail(kProperty, this)
        }

        override fun toValidationDto() = InstantValidationDto(ValidationType.Max, limit)

    }

    inner class Min(@PublicApi val limit: Instant) : ValidationRule<Instant?> {

        override fun validate(value: Instant?, report: ValidityReport) {
            if (value != null && value < limit) report.fail(kProperty, this)
        }

        override fun toValidationDto() = InstantValidationDto(ValidationType.Min, limit)

    }

    inner class Before(@PublicApi val limit: Instant) : ValidationRule<Instant?> {

        override fun validate(value: Instant?, report: ValidityReport) {
            if (value != null && value >= limit) report.fail(kProperty, this)
        }

        override fun toValidationDto() = InstantValidationDto(ValidationType.Before, limit)

    }

    inner class After(@PublicApi val limit: Instant) : ValidationRule<Instant?> {

        override fun validate(value: Instant?, report: ValidityReport) {
            if (value != null && value <= limit) report.fail(kProperty, this)
        }

        override fun toValidationDto() = InstantValidationDto(ValidationType.After, limit)

    }

    inner class NotEquals(@PublicApi val invalidValue: Instant) : ValidationRule<Instant?> {

        override fun validate(value: Instant?, report: ValidityReport) {
            if (value == invalidValue) report.fail(kProperty, this)
        }

        override fun toValidationDto() = InstantValidationDto(ValidationType.NotEquals, invalidValue)

    }

    @PublicApi
    infix fun max(limit: Instant): OptInstantValidationRuleList {
        rules += Max(limit)
        return this
    }

    @PublicApi
    infix fun min(limit: Instant): OptInstantValidationRuleList {
        rules += Min(limit)
        return this
    }

    @PublicApi
    infix fun before(limit: Instant): OptInstantValidationRuleList {
        rules += Before(limit)
        return this
    }

    @PublicApi
    infix fun after(limit: Instant): OptInstantValidationRuleList {
        rules += After(limit)
        return this
    }

    @PublicApi
    infix fun notEquals(invalidValue: Instant): OptInstantValidationRuleList {
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
    infix fun default(value: Instant?): OptInstantValidationRuleList {
        defaultValue = value
        return this
    }

    override fun setDefault() {
        kProperty.set(defaultValue)
    }

    override fun isOptional() = true

    override fun push(dto: PropertyDto) {
        require(dto is OptInstantPropertyDto)
        kProperty.set(dto.value)
    }

    override fun toPropertyDto() = OptInstantPropertyDto(
        kProperty.name,
        rules.map { it.toValidationDto() },
        defaultValue,
        kProperty.get()
    )

}