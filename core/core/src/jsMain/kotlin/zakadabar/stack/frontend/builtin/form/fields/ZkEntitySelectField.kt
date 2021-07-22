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

import zakadabar.stack.data.BaseBo
import zakadabar.stack.data.entity.EntityBo
import zakadabar.stack.data.entity.EntityId
import zakadabar.stack.frontend.builtin.form.ZkForm
import zakadabar.stack.frontend.util.by
import zakadabar.stack.frontend.util.io
import zakadabar.stack.frontend.util.newInstance
import zakadabar.stack.util.PublicApi
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty0

open class ZkEntitySelectField<T : BaseBo, ST : EntityBo<ST>>(
    form: ZkForm<T>,
    val prop: KMutableProperty0<EntityId<ST>>,
    val entityClass : KClass<ST>
) : ZkSelect2Base<T, EntityId<ST>>(form, prop.name) {

    lateinit var selectBy : (ST) -> String

    override suspend fun getOptions(): List<Pair<EntityId<ST>, String>> {
        val comm = entityClass.newInstance().comm()
        return if (readOnly) {
            prop.get().let { listOf(it to selectBy(comm.read(it))) }
        } else {
            comm.all().by(selectBy)
        }
    }

    override fun fromString(string: String): EntityId<ST> {
        return EntityId(string)
    }

    override fun getPropValue() = prop.get()

    override fun setPropValue(value: Pair<EntityId<ST>, String>?) {
        if (value == null) {
            invalidInput = true
        } else {
            invalidInput = false
            prop.set(value.first)
        }
        form.validate()
    }

    infix fun options(func: ZkEntitySelectField<T, ST>.() ->  Unit): ZkEntitySelectField<T, ST> {
        func()
        io {
            items = getOptions()
            render(getPropValue())
        }
        return this
    }

    @PublicApi
    infix fun selectBy(func: (ST) -> String): ZkEntitySelectField<T, ST> {
        selectBy = func
        return this
    }

    infix fun readOnly(value: Boolean): ZkEntitySelectField<T, ST> {
        readOnly = value
        return this
    }

}