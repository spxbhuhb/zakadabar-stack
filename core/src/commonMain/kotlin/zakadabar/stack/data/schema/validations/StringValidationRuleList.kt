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
import zakadabar.stack.data.schema.dto.*
import zakadabar.stack.util.PublicApi
import kotlin.reflect.KMutableProperty0

class StringValidationRuleList(val kProperty: KMutableProperty0<String>) : ValidationRuleList<String> {

    var defaultValue = ""

    private val rules = mutableListOf<ValidationRule<String>>()

    inner class Max(@PublicApi val limit: Int) : ValidationRule<String> {

        override fun validate(value: String, report: ValidityReport) {
            if (value.length > limit) report.fail(kProperty, this)
        }

        override fun toValidationDto() = StringValidationIntDto(ValidationType.Max, limit)

    }

    inner class Min(@PublicApi val limit: Int) : ValidationRule<String> {

        override fun validate(value: String, report: ValidityReport) {
            if (value.length < limit) report.fail(kProperty, this)
        }

        override fun toValidationDto() = StringValidationIntDto(ValidationType.Min, limit)

    }

    inner class NotEquals(@PublicApi val invalidValue: String) : ValidationRule<String> {

        override fun validate(value: String, report: ValidityReport) {
            if (value == invalidValue) report.fail(kProperty, this)
        }

        override fun toValidationDto() = StringValidationStringDto(ValidationType.NotEquals, invalidValue)

    }

    inner class Blank(@PublicApi val validValue: Boolean) : ValidationRule<String> {

        override fun validate(value: String, report: ValidityReport) {
            if (value.isBlank() != validValue) report.fail(kProperty, this)
        }

        override fun toValidationDto() = StringValidationBooleanDto(ValidationType.Blank, validValue)

    }

    inner class Format(@PublicApi val pattern : String) : ValidationRule<String> {

        override fun validate(value: String, report: ValidityReport) {
            if (Regex(pattern).matchEntire(value) == null) report.fail(kProperty, this)
        }

        override fun toValidationDto() = StringValidationStringDto(ValidationType.Format, pattern)
    }

    @PublicApi
    infix fun max(limit: Int): StringValidationRuleList {
        rules += Max(limit)
        return this
    }

    @PublicApi
    infix fun min(limit: Int): StringValidationRuleList {
        rules += Min(limit)
        return this
    }

    @PublicApi
    infix fun notEquals(invalidValue: String): StringValidationRuleList {
        rules += NotEquals(invalidValue)
        return this
    }

    @PublicApi
    infix fun blank(blank: Boolean): StringValidationRuleList {
        rules += Blank(blank)
        return this
    }

    @PublicApi
    infix fun format(pattern: String): StringValidationRuleList {
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
    infix fun default(value: String): StringValidationRuleList {
        defaultValue = value
        return this
    }

    override fun setDefault() {
        kProperty.set(defaultValue)
    }

    override fun isOptional() = false

    override fun push(dto: PropertyDto) {
        require(dto is StringPropertyDto)
        kProperty.set(dto.value)
    }

    override fun toPropertyDto() = StringPropertyDto(
        kProperty.name,
        emptyList(),
        defaultValue,
        kProperty.get()
    )
}