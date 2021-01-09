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

import kotlinx.datetime.Instant
import zakadabar.stack.util.PublicApi
import kotlin.reflect.KProperty0

class InstantValidationRuleList(val kProperty: KProperty0<Instant>) : ValidationRuleList<Instant> {

    private val rules = mutableListOf<ValidationRule<Instant>>()

    inner class Max(@PublicApi val limit: Instant) : ValidationRule<Instant> {
        override fun validate(value: Instant, report: ValidityReport) {
            if (value > limit) report.fail(kProperty, this)
        }
    }

    inner class Min(@PublicApi val limit: Instant) : ValidationRule<Instant> {
        override fun validate(value: Instant, report: ValidityReport) {
            if (value < limit) report.fail(kProperty, this)
        }
    }

    inner class Before(@PublicApi val limit: Instant) : ValidationRule<Instant> {
        override fun validate(value: Instant, report: ValidityReport) {
            if (value >= limit) report.fail(kProperty, this)
        }
    }

    inner class After(@PublicApi val limit: Instant) : ValidationRule<Instant> {
        override fun validate(value: Instant, report: ValidityReport) {
            if (value <= limit) report.fail(kProperty, this)
        }
    }

    inner class NotEquals(@PublicApi val invalidValue: Instant) : ValidationRule<Instant> {
        override fun validate(value: Instant, report: ValidityReport) {
            if (value == invalidValue) report.fail(kProperty, this)
        }
    }

    @PublicApi
    infix fun max(limit: Instant): InstantValidationRuleList {
        rules += Max(limit)
        return this
    }

    @PublicApi
    infix fun min(limit: Instant): InstantValidationRuleList {
        rules += Min(limit)
        return this
    }

    @PublicApi
    infix fun before(limit: Instant): InstantValidationRuleList {
        rules += Before(limit)
        return this
    }

    @PublicApi
    infix fun after(limit: Instant): InstantValidationRuleList {
        rules += After(limit)
        return this
    }

    @PublicApi
    infix fun notEquals(invalidValue: Instant): InstantValidationRuleList {
        rules += NotEquals(invalidValue)
        return this
    }

    override fun validate(report: ValidityReport) {
        val value = kProperty.get()
        for (rule in rules) {
            rule.validate(value, report)
        }
    }
}