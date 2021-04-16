/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.table.actions

import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.button.ZkIconButton
import zakadabar.stack.frontend.builtin.layout.ZkLayoutStyles
import zakadabar.stack.frontend.builtin.standalone.ZkStandaloneInput
import zakadabar.stack.frontend.resources.ZkIcons
import zakadabar.stack.frontend.util.plusAssign

open class ZkSearchAction(
    private val onExecute: (searchText: String) -> Unit
) : ZkElement() {

    override fun onCreate() {
        classList += ZkLayoutStyles.row

        + ZkStandaloneInput(onChange = onExecute, enter = true) marginRight 8
        + ZkIconButton(ZkIcons.search, buttonSize = 24) {
            val value = this@ZkSearchAction[ZkStandaloneInput::class].value
            onExecute(value)
        }
    }
}