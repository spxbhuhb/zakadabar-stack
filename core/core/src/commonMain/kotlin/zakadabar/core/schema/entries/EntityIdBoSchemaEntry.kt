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
import zakadabar.core.data.EntityId
import zakadabar.core.schema.BoPropertyConstraintImpl
import zakadabar.core.schema.BoSchemaEntry
import zakadabar.core.schema.BoSchemaEntryExtension
import zakadabar.core.schema.ValidityReport
import zakadabar.core.schema.descriptor.*
import zakadabar.core.util.PublicApi
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty0

class EntityIdBoSchemaEntry<T : Any>(
    val kClass: KClass<T>,
    override val kProperty: KMutableProperty0<EntityId<T>>
) : BoSchemaEntry<EntityId<T>, EntityIdBoSchemaEntry<T>> {

    override val rules = mutableListOf<BoPropertyConstraintImpl<EntityId<T>>>()

    override val extensions = mutableListOf<BoSchemaEntryExtension<EntityId<T>>>()

    override var defaultValue: EntityId<T> = EntityId()

    override fun validate(report: ValidityReport) {
        val value = kProperty.get()
        if (value.isEmpty()) {
            if (kProperty.name != "id" || ! report.allowEmptyId) {
                report.fail(kProperty, BooleanBoConstraint(BoConstraintType.Empty, false))
            }
        } else {
            for (rule in rules) {
                rule.validate(value, report)
            }
        }
    }

    @Deprecated("EOL: 2021.7.1  -  use '?' in the field declaration", level = DeprecationLevel.ERROR)
    inner class Empty(@PublicApi val validValue: Boolean) : BoPropertyConstraintImpl<EntityId<*>> {

        override fun validate(value: EntityId<*>, report: ValidityReport) {
            if (value.isEmpty() != validValue) report.fail(kProperty, this)
        }

        override fun toBoConstraint() = BooleanBoConstraint(BoConstraintType.Empty, validValue)

    }

    @PublicApi
    infix fun default(value: EntityId<T>): EntityIdBoSchemaEntry<T> {
        defaultValue = value
        return this
    }

    override fun setDefault() {
        kProperty.set(defaultValue)
    }

    override fun decodeFromText(text : String?) : EntityId<T> {
        requireNotNull(text)
        return EntityId(text)
    }

    override fun setFromText(text: String?) {
        kProperty.set(decodeFromText(text))
    }

    override fun isOptional() = false

    override fun push(bo: BoProperty) {
        require(bo is EntityIdBoProperty)
        @Suppress("UNCHECKED_CAST") // FIXME clarify record id type erasure
        kProperty.set(bo.value as EntityId<T>)
    }

    @Suppress("UNCHECKED_CAST") // should work, lost in generics hell
    override fun toBoProperty() = EntityIdBoProperty(
        kProperty.name,
        isOptional(),
        constraints(),
        kClass.simpleName!!,
        defaultValue as EntityId<BaseBo>,
        kProperty.get() as EntityId<BaseBo>
    )

    override fun constraints() = emptyList<BoConstraint>()

}