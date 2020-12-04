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
package zakadabar.stack.data.schema

import zakadabar.stack.util.PublicApi
import kotlin.reflect.KProperty0

class StringValidationRuleList(val kProperty: KProperty0<String>) : ValidationRuleList<String> {

    private val rules = mutableListOf<ValidationRule<String>>()

    inner class Max(@PublicApi val limit: Int) : ValidationRule<String> {
        override fun validate(value: String, report: ValidityReport) {
            if (value.length > limit) report.fail(kProperty, this)
        }
    }

    inner class Min(@PublicApi val limit: Int) : ValidationRule<String> {
        override fun validate(value: String, report: ValidityReport) {
            if (value.length < limit) report.fail(kProperty, this)
        }
    }

    inner class NotEquals(@PublicApi val invalidValue: String) : ValidationRule<String> {
        override fun validate(value: String, report: ValidityReport) {
            if (value == invalidValue) report.fail(kProperty, this)
        }
    }


    inner class Blank(@PublicApi val validValue: Boolean) : ValidationRule<String> {
        override fun validate(value: String, report: ValidityReport) {
            if (value.isBlank() != validValue) report.fail(kProperty, this)
        }
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

    override fun validate(report: ValidityReport) {
        val value = kProperty.get()
        for (rule in rules) {
            rule.validate(value, report)
        }
    }
}