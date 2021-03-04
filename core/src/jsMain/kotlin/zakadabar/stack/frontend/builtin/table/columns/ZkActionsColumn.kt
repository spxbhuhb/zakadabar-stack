/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.table.columns

import org.w3c.dom.set
import zakadabar.stack.data.DtoBase
import zakadabar.stack.frontend.builtin.table.ZkTable
import zakadabar.stack.frontend.builtin.table.ZkTableStyles
import zakadabar.stack.frontend.elements.ZkElement
import zakadabar.stack.frontend.elements.plusAssign
import zakadabar.stack.frontend.resources.CoreStrings

open class ZkActionsColumn<T : DtoBase>(
    override val table: ZkTable<T>
) : ZkColumn<T> {

    override var label = CoreStrings.actions

    override fun render(builder: ZkElement, index: Int, row: T) {
        with(builder) {
            currentElement.classList += ZkTableStyles.action
            currentElement.dataset["action"] = "update"
            + CoreStrings.details
        }
    }

}