/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.table.columns

import zakadabar.stack.data.BaseBo
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.table.ZkTable

open class ZkCustomColumn<T : BaseBo>(
    table: ZkTable<T>
) : ZkColumn<T>(table) {

    lateinit var render: ZkElement.(T) -> Unit

    override fun render(builder: ZkElement, index: Int, row: T) {
        builder.render(row)
    }

}