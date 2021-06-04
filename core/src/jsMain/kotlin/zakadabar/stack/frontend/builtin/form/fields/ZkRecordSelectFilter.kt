/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.form.fields

import zakadabar.stack.data.BaseBo
import zakadabar.stack.data.entity.EntityId
import zakadabar.stack.frontend.builtin.form.ZkForm

open class ZkRecordSelectFilter<T : BaseBo>(
    form: ZkForm<T>,
    sortOptions: Boolean = true,
    label: String? = null,
    var getValue: () -> EntityId<*>?,
    var options: suspend () -> List<Pair<EntityId<*>, String>>,
    onSelected: (Pair<EntityId<*>, String>?) -> Unit
) : ZkSelectBase<T, EntityId<*>>(form, "", sortOptions, options, onSelected) {

    init {
        // FIXME this is not right, but haven't had time to fix yet
        if (label != null) this.labelText = label
    }

    override fun fromString(string: String): EntityId<*> {
        return items.first().first
    }

    override fun getPropValue() = getValue()

    override fun setPropValue(value: Pair<EntityId<*>, String>?) {
        // do nothing here, onSelected will be called by ZkSelectBase
    }

}
