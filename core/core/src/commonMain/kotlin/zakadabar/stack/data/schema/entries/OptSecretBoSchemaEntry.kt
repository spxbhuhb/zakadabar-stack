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
package zakadabar.core.data.schema.entries

import zakadabar.core.data.builtin.misc.Secret
import zakadabar.core.data.schema.BoPropertyConstraintImpl
import zakadabar.core.data.schema.BoSchemaEntry
import zakadabar.core.data.schema.ValidityReport
import zakadabar.core.data.schema.descriptor.*
import zakadabar.core.util.PublicApi
import kotlin.reflect.KMutableProperty0

class OptSecretBoSchemaEntry(val kProperty: KMutableProperty0<Secret?>) : BoSchemaEntry<Secret?> {

    var defaultValue: Secret? = null

    private val rules = mutableListOf<BoPropertyConstraintImpl<Secret?>>()

    inner class Max(@PublicApi val limit: Int) : BoPropertyConstraintImpl<Secret?> {

        override fun validate(value: Secret?, report: ValidityReport) {
            if (value != null && value.value.length > limit) report.fail(kProperty, this)
        }

        override fun toBoConstraint() = IntBoConstraint(BoConstraintType.Max, limit)

    }

    inner class Min(@PublicApi val limit: Int) : BoPropertyConstraintImpl<Secret?> {

        override fun validate(value: Secret?, report: ValidityReport) {
            if (value != null && value.value.length < limit) report.fail(kProperty, this)
        }

        override fun toBoConstraint() = IntBoConstraint(BoConstraintType.Min, limit)

    }

    inner class Blank(@PublicApi val validValue: Boolean) : BoPropertyConstraintImpl<Secret?> {

        override fun validate(value: Secret?, report: ValidityReport) {
            if (validValue) return // there is nothing to check when blank is allowed

            // here blank value is not allowed

            if (value == null || value.value.isBlank()) {
                report.fail(kProperty, this)
            }
        }

        override fun toBoConstraint() = BooleanBoConstraint(BoConstraintType.Blank, validValue)

    }

    @PublicApi
    infix fun max(limit: Int): OptSecretBoSchemaEntry {
        rules += Max(limit)
        return this
    }

    @PublicApi
    infix fun min(limit: Int): OptSecretBoSchemaEntry {
        rules += Min(limit)
        return this
    }

    @PublicApi
    infix fun blank(blank: Boolean): OptSecretBoSchemaEntry {
        rules += Blank(blank)
        return this
    }

    override fun validate(report: ValidityReport) {
        val value = kProperty.get()
        for (rule in rules) {
            rule.validate(value, report)
        }
    }

    @PublicApi
    infix fun default(value: Secret?): OptSecretBoSchemaEntry {
        defaultValue = value
        return this
    }

    override fun setDefault() {
        kProperty.set(defaultValue)
    }

    override fun decodeFromText(text : String?) : Secret? {
        return text?.let { Secret(it) }
    }

    override fun setFromText(text: String?) {
        kProperty.set(decodeFromText(text))
    }

    override fun isOptional() = true

    override fun push(bo: BoProperty) {
        require(bo is SecretBoProperty)
        kProperty.set(bo.value)
    }

    override fun toBoProperty() = SecretBoProperty(
        kProperty.name,
        isOptional(),
        constraints(),
        defaultValue,
        kProperty.get()
    )

    override fun constraints() = rules.map { it.toBoConstraint() }

}