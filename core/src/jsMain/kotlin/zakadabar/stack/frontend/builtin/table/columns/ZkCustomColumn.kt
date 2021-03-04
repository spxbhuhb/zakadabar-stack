/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.table.columns

import zakadabar.stack.data.DtoBase
import zakadabar.stack.frontend.builtin.table.ZkTable
import zakadabar.stack.frontend.elements.ZkElement

open class ZkCustomColumn<T : DtoBase>(
    override val table: ZkTable<T>
) : ZkColumn<T> {

    override var label = ""

    lateinit var render: ZkElement.(T) -> Unit

    override fun render(builder: ZkElement, index: Int, row: T) {
        builder.render(row)
    }

}