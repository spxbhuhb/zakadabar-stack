/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package zakadabar.stack.frontend.builtin.form.fields

import zakadabar.stack.data.entity.EntityBo
import zakadabar.stack.data.entity.EntityId
import kotlin.reflect.KMutableProperty0

open class ZkOptEntitySelectField<ST : EntityBo<ST>>(
    context : ZkFieldContext,
    val prop: KMutableProperty0<EntityId<ST>?>
) : ZkSelectBase<EntityId<ST>>(context, prop.name) {

    override fun fromString(string: String): EntityId<ST> {
        return EntityId(string)
    }

    override fun getPropValue() = prop.get()

    override fun setPropValue(value: Pair<EntityId<ST>, String>?) {
        prop.set(value?.first)
        context.validate()
    }

}