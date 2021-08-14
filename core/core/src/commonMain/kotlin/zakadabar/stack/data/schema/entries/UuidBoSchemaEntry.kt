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

import zakadabar.core.data.schema.BoSchemaEntry
import zakadabar.core.data.schema.ValidityReport
import zakadabar.core.data.schema.descriptor.BoConstraint
import zakadabar.core.data.schema.descriptor.BoProperty
import zakadabar.core.data.schema.descriptor.UuidBoProperty
import zakadabar.core.util.PublicApi
import zakadabar.core.util.UUID
import kotlin.reflect.KMutableProperty0

class UuidBoSchemaEntry(val kProperty: KMutableProperty0<UUID>) : BoSchemaEntry<UUID> {

    var defaultValue = UUID.NIL

    override fun validate(report: ValidityReport) {}

    @PublicApi
    infix fun default(value: UUID): UuidBoSchemaEntry {
        defaultValue = value
        return this
    }

    override fun setDefault() {
        kProperty.set(defaultValue)
    }

    override fun decodeFromText(text : String?) : UUID {
        return UUID(text!!)
    }

    override fun setFromText(text: String?) {
        kProperty.set(decodeFromText(text))
    }

    override fun isOptional() = false

    override fun push(bo: BoProperty) {
        require(bo is UuidBoProperty)
        kProperty.set(bo.value!!)
    }

    override fun toBoProperty() = UuidBoProperty(
        kProperty.name,
        isOptional(),
        constraints(),
        defaultValue,
        kProperty.get()
    )

    override fun constraints() = emptyList<BoConstraint>()

}