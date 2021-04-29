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

import zakadabar.stack.data.schema.ValidationRuleList
import zakadabar.stack.data.schema.ValidityReport
import zakadabar.stack.data.schema.dto.BooleanPropertyDto
import zakadabar.stack.data.schema.dto.OptUuidPropertyDto
import zakadabar.stack.data.schema.dto.PropertyDto
import zakadabar.stack.util.PublicApi
import zakadabar.stack.util.UUID
import kotlin.reflect.KMutableProperty0

class OptUuidValidationRuleList(val kProperty: KMutableProperty0<UUID?>) : ValidationRuleList<UUID?> {

    var defaultValue: UUID? = null

    override fun validate(report: ValidityReport) {}

    @PublicApi
    infix fun default(value: UUID?): OptUuidValidationRuleList {
        defaultValue = value
        return this
    }

    override fun setDefault() {
        kProperty.set(defaultValue)
    }

    override fun isOptional() = true

    override fun push(dto: PropertyDto) {
        require(dto is OptUuidPropertyDto)
        kProperty.set(dto.value)
    }

    override fun toPropertyDto() = OptUuidPropertyDto(
        kProperty.name,
        emptyList(),
        defaultValue,
        kProperty.get()
    )
}