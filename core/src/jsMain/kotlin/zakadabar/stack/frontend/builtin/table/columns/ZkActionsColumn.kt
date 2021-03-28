/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.table.columns

import org.w3c.dom.set
import zakadabar.stack.data.DtoBase
import zakadabar.stack.frontend.application.ZkApplication.strings
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.table.ZkTable
import zakadabar.stack.frontend.builtin.table.ZkTableStyles
import zakadabar.stack.frontend.util.plusAssign

open class ZkActionsColumn<T : DtoBase>(
    override val table: ZkTable<T>
) : ZkColumn<T> {

    override var label = strings.actions

    override fun render(builder: ZkElement, index: Int, row: T) {
        with(builder) {
            buildElement.classList += ZkTableStyles.action
            buildElement.dataset["action"] = "update"
            + strings.details
        }
    }

}