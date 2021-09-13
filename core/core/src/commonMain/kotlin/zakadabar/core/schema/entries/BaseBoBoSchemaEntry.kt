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

import zakadabar.core.data.BaseBo
import zakadabar.core.schema.BoPropertyConstraintImpl
import zakadabar.core.schema.BoSchemaEntry
import zakadabar.core.schema.BoSchemaEntryExtension
import zakadabar.core.schema.ValidityReport
import zakadabar.core.schema.descriptor.BaseBoBoProperty
import zakadabar.core.schema.descriptor.BoConstraint
import zakadabar.core.schema.descriptor.BoProperty
import zakadabar.core.util.PublicApi
import kotlin.reflect.KMutableProperty0

class BaseBoBoSchemaEntry<T : BaseBo>(
    override val kProperty: KMutableProperty0<T>,
    override var defaultValue : T
) : BoSchemaEntry<T, BaseBoBoSchemaEntry<T>> {

    override val rules = mutableListOf<BoPropertyConstraintImpl<T>>()

    override val extensions = mutableListOf<BoSchemaEntryExtension<T>>()

    override fun validate(report: ValidityReport) {
        kProperty.get().schema().validate()
    }

    @PublicApi
    infix fun default(value: T): BaseBoBoSchemaEntry<T> {
        defaultValue = value
        return this
    }

    override fun setDefault() {
        kProperty.set(defaultValue)
    }

    override fun isOptional() = false

    override fun push(bo: BoProperty) {
        require(bo is BaseBoBoProperty)
        kProperty.get().schema().push(bo.value !!)
    }

    override fun toBoProperty() = BaseBoBoProperty(
        kProperty.name,
        isOptional(),
        constraints(),
        kProperty.get().schema().toBoDescriptor()
    )

    override fun constraints() = emptyList<BoConstraint>()

}