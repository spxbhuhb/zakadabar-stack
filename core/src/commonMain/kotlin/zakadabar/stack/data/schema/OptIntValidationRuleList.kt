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
import kotlin.reflect.KMutableProperty0

class OptIntValidationRuleList(val kProperty: KMutableProperty0<Int?>) : ValidationRuleList<Int?> {

    var defaultValue: Int? = null

    private val rules = mutableListOf<ValidationRule<Int?>>()

    inner class Max(@PublicApi val limit: Int) : ValidationRule<Int?> {
        override fun validate(value: Int?, report: ValidityReport) {
            if (value != null && value > limit) report.fail(kProperty, this)
        }
    }

    inner class Min(@PublicApi val limit: Int) : ValidationRule<Int?> {
        override fun validate(value: Int?, report: ValidityReport) {
            if (value != null && value < limit) report.fail(kProperty, this)
        }
    }

    inner class NotEquals(@PublicApi val invalidValue: Int?) : ValidationRule<Int?> {
        override fun validate(value: Int?, report: ValidityReport) {
            if (value == invalidValue) report.fail(kProperty, this)
        }
    }

    @PublicApi
    infix fun max(limit: Int): OptIntValidationRuleList {
        rules += Max(limit)
        return this
    }

    @PublicApi
    infix fun min(limit: Int): OptIntValidationRuleList {
        rules += Min(limit)
        return this
    }

    @PublicApi
    infix fun notEquals(invalidValue: Int?): OptIntValidationRuleList {
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
    infix fun default(value: Int?): OptIntValidationRuleList {
        defaultValue = value
        return this
    }

    override fun setDefault() {
        kProperty.set(defaultValue)
    }

    override fun isOptional() = true

}