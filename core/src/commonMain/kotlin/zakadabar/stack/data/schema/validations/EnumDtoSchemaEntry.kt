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

import zakadabar.stack.data.schema.DtoSchemaEntry
import zakadabar.stack.data.schema.ValidityReport
import zakadabar.stack.data.schema.descriptor.EnumPropertyDto
import zakadabar.stack.data.schema.descriptor.PropertyDto
import zakadabar.stack.util.PublicApi
import kotlin.reflect.KMutableProperty0

class EnumDtoSchemaEntry<E : Enum<E>>(
    val kProperty: KMutableProperty0<E>,
    val values : Array<E>
) : DtoSchemaEntry<E> {

    var defaultValue = values.first()

    override fun validate(report: ValidityReport) {}

    @PublicApi
    infix fun default(value: E): EnumDtoSchemaEntry<E> {
        defaultValue = value
        return this
    }

    override fun setDefault() {
        kProperty.set(defaultValue)
    }

    override fun isOptional() = false

    override fun push(dto: PropertyDto) {
        require(dto is EnumPropertyDto)
        kProperty.set(values.firstOrNull { it.name == dto.value } ?: throw IllegalArgumentException("value for ${kProperty.name} is invalid"))
    }

    override fun toPropertyDto() = EnumPropertyDto(
        kProperty.name,
        isOptional(),
        emptyList(),
        values.map { it.name },
        defaultValue.name,
        kProperty.get().name
    )

}