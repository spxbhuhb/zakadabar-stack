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

import zakadabar.stack.data.schema.BoSchemaEntry
import zakadabar.stack.data.schema.ValidityReport
import zakadabar.stack.data.schema.descriptor.BoProperty
import zakadabar.stack.data.schema.descriptor.EnumBoProperty
import zakadabar.stack.util.PublicApi
import kotlin.reflect.KMutableProperty0

class EnumBoSchemaEntry<E : Enum<E>>(
    val kProperty: KMutableProperty0<E>,
    val values : Array<E>
) : BoSchemaEntry<E> {

    var defaultValue = values.first()

    override fun validate(report: ValidityReport) {}

    @PublicApi
    infix fun default(value: E): EnumBoSchemaEntry<E> {
        defaultValue = value
        return this
    }

    override fun setDefault() {
        kProperty.set(defaultValue)
    }

    override fun isOptional() = false

    override fun push(bo: BoProperty) {
        require(bo is EnumBoProperty)
        kProperty.set(values.firstOrNull { it.name == bo.value } ?: throw IllegalArgumentException("value for ${kProperty.name} is invalid"))
    }

    override fun toBoProperty() = EnumBoProperty(
        kProperty.name,
        isOptional(),
        emptyList(),
        values.first()::class.simpleName!!,
        values.map { it.name },
        defaultValue.name,
        kProperty.get().name
    )

}