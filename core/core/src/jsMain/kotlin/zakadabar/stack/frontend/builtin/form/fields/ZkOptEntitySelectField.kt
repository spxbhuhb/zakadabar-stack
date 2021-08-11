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
package zakadabar.stack.frontend.builtin.form.fields

import zakadabar.stack.data.entity.EntityBo
import zakadabar.stack.data.entity.EntityId
import zakadabar.stack.frontend.util.by
import zakadabar.stack.frontend.util.io
import zakadabar.stack.frontend.util.newInstance
import zakadabar.stack.util.PublicApi
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty0

// FIXME merge common functions with ZkEntitySelectField

open class ZkOptEntitySelectField<ST : EntityBo<ST>>(
    context : ZkFieldContext,
    val prop: KMutableProperty0<EntityId<ST>?>,
    val entityClass : KClass<ST>
) : ZkSelect2Base<EntityId<ST>>(context, prop.name) {

    lateinit var selectBy : (ST) -> String

    override suspend fun getOptions(): List<Pair<EntityId<ST>, String>> {
        val comm = entityClass.newInstance().comm()
        return if (readOnly) {
            prop.get()?.let { listOf(it to selectBy(comm.read(it))) } ?: emptyList()
        } else {
            comm.all().by(selectBy)
        }
    }

    override fun fromString(string: String): EntityId<ST> {
        return EntityId(string)
    }

    override fun getPropValue() = prop.get()

    override fun setPropValue(value: Pair<EntityId<ST>, String>?) {
        prop.set(value?.first)
        context.validate()
    }

    override fun onAfterOptions() {
        io {
            items = getOptions()
            render(getPropValue())
        }
    }

    @PublicApi
    infix fun ZkOptEntitySelectField<ST>?.selectBy(func: (ST) -> String): ZkOptEntitySelectField<ST>? {
        if (this == null) return this
        selectBy = func
        return this
    }

    infix fun ZkOptEntitySelectField<ST>?.readOnly(value: Boolean): ZkOptEntitySelectField<ST>? {
        if (this == null) return this
        readOnly = value
        return this
    }

}