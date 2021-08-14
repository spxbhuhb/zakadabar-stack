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

import zakadabar.core.data.schema.BoPropertyConstraintImpl
import zakadabar.core.data.schema.BoSchemaEntry
import zakadabar.core.data.schema.ValidityReport
import zakadabar.core.data.schema.descriptor.BoConstraint
import zakadabar.core.data.schema.descriptor.BoProperty

class CustomBoSchemaEntry : BoSchemaEntry<Unit> {

    val name : String
    private val rules = mutableListOf<BoPropertyConstraintImpl<Unit>>()

    @Deprecated("EOL: 2021.8.1  -  use function without the rule parameter")
    constructor(function: (report: ValidityReport, rule: BoPropertyConstraintImpl<Unit>) -> Unit) {
        name = "-"
        rules += CustomValidation(function)
    }

    constructor(constraintName : String, function: (constraintName : String, report: ValidityReport) -> Unit) {
        this.name = constraintName
        rules += CustomConstraint(function)
    }

    inner class CustomValidation(val function: (report: ValidityReport, rule: BoPropertyConstraintImpl<Unit>) -> Unit) : BoPropertyConstraintImpl<Unit> {
        override fun validate(value: Unit, report: ValidityReport) {
            function(report, this)
        }

        override fun toBoConstraint(): BoConstraint {
            throw NotImplementedError("serialization of custom validations is not supported")
        }
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