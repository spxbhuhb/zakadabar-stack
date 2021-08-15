/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package zakadabar.core.frontend.builtin.form.fields

import zakadabar.core.data.EntityBo
import zakadabar.core.data.EntityId
import kotlin.reflect.KMutableProperty0

open class ZkEntitySelectField<ST : EntityBo<ST>>(
    context : ZkFieldContext,
    val prop: KMutableProperty0<EntityId<ST>>
) : ZkSelectBase<EntityId<ST>>(context, prop.name) {

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
        context.validate()
    }

}