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
package zakadabar.stack.data.schema.entries

import zakadabar.stack.data.schema.DtoPropertyConstraint
import zakadabar.stack.data.schema.DtoSchemaEntry
import zakadabar.stack.data.schema.ValidityReport
import zakadabar.stack.data.schema.descriptor.ConstraintDto
import zakadabar.stack.data.schema.descriptor.PropertyDto

class CustomDtoSchemaEntry(function: (report: ValidityReport, rule: DtoPropertyConstraint<Unit>) -> Unit) : DtoSchemaEntry<Unit> {

    private val rules = mutableListOf<DtoPropertyConstraint<Unit>>(CustomValidation(function))

    inner class CustomValidation(val function: (report: ValidityReport, rule: DtoPropertyConstraint<Unit>) -> Unit) : DtoPropertyConstraint<Unit> {
        override fun validate(value: Unit, report: ValidityReport) {
            function(report, this)
        }

        override fun toValidationDto(): ConstraintDto {
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

    override fun push(dto: PropertyDto) {

    }

    override fun toPropertyDto(): PropertyDto? = null
}