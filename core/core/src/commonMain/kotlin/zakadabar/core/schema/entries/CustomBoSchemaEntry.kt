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
package zakadabar.core.schema.entries

import zakadabar.core.schema.BoPropertyConstraintImpl
import zakadabar.core.schema.BoSchemaEntry
import zakadabar.core.schema.BoSchemaEntryExtension
import zakadabar.core.schema.ValidityReport
import zakadabar.core.schema.descriptor.BoConstraint
import zakadabar.core.schema.descriptor.BoProperty
import kotlin.reflect.KProperty

class CustomBoSchemaEntry(
    val name: String,
    function: (constraintName: String, report: ValidityReport) -> Unit
) : BoSchemaEntry<Unit, CustomBoSchemaEntry> {

    override val kProperty: KProperty<Unit>
        get() = throw IllegalStateException("custom schema entries have no property")

    override val rules = mutableListOf<BoPropertyConstraintImpl<Unit>>()

    override val extensions = mutableListOf<BoSchemaEntryExtension<Unit>>()

    override var defaultValue = Unit

    init {
        rules += CustomConstraint(function)
    }

    inner class CustomConstraint(val function: (constraintName : String, report: ValidityReport) -> Unit) : BoPropertyConstraintImpl<Unit> {
        override fun validate(value: Unit, report: ValidityReport) {
            function(name, report)
        }

        override fun toBoConstraint(): BoConstraint {
            throw NotImplementedError("serialization of custom validations is not supported")
        }
    }

    override fun validate(report: ValidityReport) {
        for (rule in rules) {
            rule.validate(Unit, report)
        }
    }

    override fun setDefault() {
    }

    override fun isOptional() = false

    override fun push(bo: BoProperty) {

    }

    override fun toBoProperty(): BoProperty? = null

    override fun constraints() = rules.map { it.toBoConstraint() }

}