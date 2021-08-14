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
import zakadabar.core.data.schema.descriptor.BooleanBoProperty
import zakadabar.core.util.PublicApi
import kotlin.reflect.KMutableProperty0

class OptBooleanBoSchemaEntry(val kProperty: KMutableProperty0<Boolean?>) : BoSchemaEntry<Boolean?> {

    var defaultValue: Boolean? = null

    override fun validate(report: ValidityReport) {}

    @PublicApi
    infix fun default(value: Boolean?): OptBooleanBoSchemaEntry {
        defaultValue = value
        return this
    }

    override fun decodeFromText(text : String?) : Boolean? {
        return text?.toBooleanStrict()
    }

    override fun setFromText(text: String?) {
        kProperty.set(decodeFromText(text))
    }

    override fun setDefault() {
        kProperty.set(defaultValue)
    }

    override fun isOptional() = true

    override fun push(bo: BoProperty) {
        require(bo is BooleanBoProperty)
        kProperty.set(bo.value)
    }

    override fun toBoProperty() = BooleanBoProperty(
        kProperty.name,
        isOptional(),
        constraints(),
        defaultValue,
        kProperty.get()
    )

    override fun constraints() = emptyList<BoConstraint>()

}