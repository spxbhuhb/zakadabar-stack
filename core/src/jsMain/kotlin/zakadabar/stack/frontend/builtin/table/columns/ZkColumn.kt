/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.table.columns

import zakadabar.stack.data.DtoBase
import zakadabar.stack.frontend.builtin.table.ZkTable
import zakadabar.stack.frontend.elements.ZkElement

interface ZkColumn<T : DtoBase> {

    var label: String
    val table: ZkTable<T>

    fun renderHeader(builder: ZkElement) {
        with(builder) {
            + th { + label }
        }
    }

    fun render(builder: ZkElement, index: Int, row: T)

    fun gridTemplate(): String {
        return "minmax(100px, 1fr)"
    }

}
