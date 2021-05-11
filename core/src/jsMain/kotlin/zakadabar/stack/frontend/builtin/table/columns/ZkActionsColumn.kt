/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.table.columns

import org.w3c.dom.set
import zakadabar.stack.data.DtoBase
import zakadabar.stack.frontend.application.ZkApplication.strings
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.table.ZkTable
import zakadabar.stack.frontend.builtin.table.zkTableStyles
import zakadabar.stack.frontend.util.plusAssign

open class ZkActionsColumn<T : DtoBase>(
    table: ZkTable<T>
) : ZkColumn<T>(table) {

    override fun onCreate() {
        label = strings.actions
        exportable = false
        super.onCreate()
    }

    override fun render(builder: ZkElement, index: Int, row: T) {
        with(builder) {
            buildElement.classList += zkTableStyles.action
            buildElement.dataset["action"] = "update"
            + strings.details
        }
    }

}