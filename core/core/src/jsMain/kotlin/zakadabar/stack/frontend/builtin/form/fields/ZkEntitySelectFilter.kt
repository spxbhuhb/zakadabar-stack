/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.form.fields

import zakadabar.stack.data.entity.EntityId

open class ZkEntitySelectFilter(
    context : ZkFieldContext,
    var getValue: () -> EntityId<*>?,
    onSelected: (Pair<EntityId<*>, String>?) -> Unit
) : ZkSelectBase< EntityId<*>>(context, "", onSelected = onSelected) {

    override fun fromString(string: String): EntityId<*> {
        return items.first().first
    }

    override fun getPropValue() = getValue()

    override fun setPropValue(value: Pair<EntityId<*>, String>?) {
        // do nothing here, onSelected will be called by ZkSelectBase
    }

}
