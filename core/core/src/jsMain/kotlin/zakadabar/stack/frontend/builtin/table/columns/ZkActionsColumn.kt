/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.frontend.builtin.table.columns

import org.w3c.dom.set
import zakadabar.core.data.BaseBo
import zakadabar.core.frontend.builtin.ZkElement
import zakadabar.core.frontend.builtin.table.ZkTable
import zakadabar.core.frontend.builtin.table.zkTableStyles
import zakadabar.core.frontend.util.plusAssign
import zakadabar.core.resources.localizedStrings

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