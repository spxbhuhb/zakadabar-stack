/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.form.fields

import zakadabar.stack.data.DtoBase
import zakadabar.stack.data.record.RecordId
import zakadabar.stack.frontend.builtin.form.ZkForm

class RecordSelectFilter<T : DtoBase>(
    form: ZkForm<T>,
    sortOptions: Boolean = true,
    label: String? = null,
    var getValue: () -> Long?,
    var options: suspend () -> List<Pair<RecordId<*>, String>>,
    var onSelected: (Pair<RecordId<*>, String>?) -> Unit
) : ValidatedSelectBase<T, RecordId<*>>(form, "", sortOptions, options) {

    init {
        if (label != null) this.label = label
    }

    override fun fromString(string: String): RecordId<*> {
        return string.toLong()
    }

    override fun getPropValue() = getValue()

    override fun setPropValue(value: Pair<RecordId<*>, String>?) {
        onSelected(value)
    }

}
