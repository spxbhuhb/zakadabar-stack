/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.table.columns

import zakadabar.core.browser.ZkElement
import zakadabar.core.browser.table.ZkTable
import zakadabar.core.data.BaseBo

open class ZkIndexColumn<T : BaseBo>(
    table: ZkTable<T>,
    val builder: ZkElement.(index : Int) -> Unit
) : ZkColumn<T>(table) {

    override var label: String = "#"
    override var exportable : Boolean = false

    override fun render(cell: ZkElement, index: Int, row: T) {
        cell.builder(index)
    }

}