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

import zakadabar.stack.data.BaseBo
import zakadabar.stack.data.entity.EntityId
import zakadabar.stack.data.schema.BoPropertyConstraintImpl
import zakadabar.stack.data.schema.BoSchemaEntry
import zakadabar.stack.data.schema.ValidityReport
import zakadabar.stack.data.schema.descriptor.*
import zakadabar.stack.util.PublicApi
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty0

class OptEntityIdBoSchemaEntry<T : Any>(
    val kClass: KClass<T>,
    val kProperty: KMutableProperty0<EntityId<T>?>
) : BoSchemaEntry<EntityId<*>?> {

    var defaultValue: EntityId<T>? = null

    private val rules = mutableListOf<BoPropertyConstraintImpl<EntityId<T>?>>()

    override fun validate(report: ValidityReport) {
        val value = kProperty.get()
        for (rule in rules) {
            rule.validate(value, report)
        }
    }

    inner class Empty(@PublicApi val validValue: Boolean) : BoPropertyConstraintImpl<EntityId<T>?> {

        override fun validate(value: EntityId<T>?, report: ValidityReport) {
            if (value?.isEmpty() != validValue) report.fail(kProperty, this)
        }

        override fun toBoConstraint() = BooleanBoConstraint(BoConstraintType.Empty, validValue)

    }

    @PublicApi
    infix fun empty(validValue: Boolean): OptEntityIdBoSchemaEntry<*> {
        rules += Empty(validValue)
        return this
    }

    @PublicApi
    infix fun default(value: EntityId<T>?): OptEntityIdBoSchemaEntry<*> {
        defaultValue = value
        return this
    }

    override fun setDefault() {
        kProperty.set(defaultValue)
    }

    override fun isOptional() = true

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