/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.table.columns

import zakadabar.core.data.BaseBo
import zakadabar.core.browser.ZkElement
import zakadabar.core.browser.table.ZkTable

open class ZkCustomColumn<T : BaseBo>(
    table: ZkTable<T>
) : ZkColumn<T>(table) {

    lateinit var render: ZkElement.(T) -> Unit
    var matcher: (row: T, string: String) -> Boolean = { _,_ -> false }

    override fun render(builder: ZkElement, index: Int, row: T) {
        builder.render(row)
    }

    override fun matches(row: T, string: String?): Boolean {
        if (string == null) return false
        return matcher(row, string)
    }

}