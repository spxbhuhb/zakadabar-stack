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

import zakadabar.stack.data.schema.DtoSchemaEntry
import zakadabar.stack.data.schema.ValidityReport
import zakadabar.stack.data.schema.descriptor.PropertyDto
import zakadabar.stack.data.schema.descriptor.UuidPropertyDto
import zakadabar.stack.util.PublicApi
import zakadabar.stack.util.UUID
import kotlin.reflect.KMutableProperty0

class UuidDtoSchemaEntry(val kProperty: KMutableProperty0<UUID>) : DtoSchemaEntry<UUID> {

    var defaultValue = UUID.NIL

    override fun validate(report: ValidityReport) {}

    @PublicApi
    infix fun default(value: UUID): UuidDtoSchemaEntry {
        defaultValue = value
        return this
    }

    override fun setDefault() {
        kProperty.set(defaultValue)
    }

    override fun isOptional() = false

    override fun push(dto: PropertyDto) {
        require(dto is UuidPropertyDto)
        kProperty.set(dto.value!!)
    }

    override fun toPropertyDto() = UuidPropertyDto(
        kProperty.name,
        isOptional(),
        emptyList(),
        defaultValue,
        kProperty.get()
    )

}