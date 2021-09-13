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
import zakadabar.core.schema.descriptor.EnumBoProperty
import zakadabar.core.util.PublicApi
import kotlin.reflect.KMutableProperty0

class EnumBoSchemaEntry<E : Enum<E>>(
    override val kProperty: KMutableProperty0<E>,
    val values : Array<E>
) : BoSchemaEntry<E, EnumBoSchemaEntry<E>> {

    override val rules = mutableListOf<BoPropertyConstraintImpl<E>>()

    override val extensions = mutableListOf<BoSchemaEntryExtension<E>>()

    override var defaultValue = values.first()

    override fun validate(report: ValidityReport) {}

    @PublicApi
    infix fun default(value: E): EnumBoSchemaEntry<E> {
        defaultValue = value
        return this
    }

    override fun setDefault() {
        kProperty.set(defaultValue)
    }

    override fun decodeFromText(text : String?) : E {
        return values.first { it.name == text }
    }

    override fun setFromText(text: String?) {
        kProperty.set(decodeFromText(text))
    }

    override fun isOptional() = false

    override fun push(bo: BoProperty) {
        require(bo is EnumBoProperty)
        kProperty.set(values.firstOrNull { it.name == bo.value } ?: throw IllegalArgumentException("value for ${kProperty.name} is invalid"))
    }

    override fun toBoProperty() = EnumBoProperty(
        kProperty.name,
        isOptional(),
        constraints(),
        values.first()::class.simpleName!!,
        values.map { it.name },
        defaultValue.name,
        kProperty.get().name
    )

    override fun constraints() = emptyList<BoConstraint>()

}