/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.table.columns

import org.w3c.dom.set
import zakadabar.stack.data.BaseBo
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.table.ZkTable
import zakadabar.stack.frontend.builtin.table.zkTableStyles
import zakadabar.stack.frontend.util.plusAssign
import zakadabar.stack.resources.localizedStrings

open class ZkActionsColumn<T : BaseBo>(
    table: ZkTable<T>
) : ZkColumn<T>(table) {

    override fun onCreate() {
        label = localizedStrings.actions
        exportable = false
        super.onCreate()
    }

    override fun render(builder: ZkElement, index: Int, row: T) {
        with(builder) {
            buildPoint.classList += zkTableStyles.action
            buildPoint.dataset["action"] = "update"
            + localizedStrings.details
        }
    }

}