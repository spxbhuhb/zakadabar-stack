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

class CustomValidationRuleList(function: (report: ValidityReport, rule: ValidationRule<Unit>) -> Unit) : ValidationRuleList<Unit> {

    private val rules = mutableListOf<ValidationRule<Unit>>(CustomValidationRule(function))

    inner class CustomValidationRule(val function: (report: ValidityReport, rule: ValidationRule<Unit>) -> Unit) : ValidationRule<Unit> {
        override fun validate(value: Unit, report: ValidityReport) {
            function(report, this)
        }
    }

    override fun validate(report: ValidityReport) {
        for (rule in rules) {
            rule.validate(Unit, report)
        }
    }

    @PublicApi
    infix fun default(value: Unit): CustomValidationRuleList {
        return this
    }

    override fun setDefault() {
    }

    override fun isOptional() = false

    override fun decodeFromString(value: String?) {
    }

}