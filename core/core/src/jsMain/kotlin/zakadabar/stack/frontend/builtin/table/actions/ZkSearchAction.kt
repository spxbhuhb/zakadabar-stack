/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.frontend.builtin.table.actions

import zakadabar.core.frontend.builtin.ZkElement
import zakadabar.core.frontend.builtin.button.ZkButton
import zakadabar.core.frontend.builtin.input.ZkTextInput
import zakadabar.core.frontend.builtin.layout.zkLayoutStyles
import zakadabar.core.frontend.resources.ZkFlavour
import zakadabar.core.frontend.resources.ZkIcons
import zakadabar.core.frontend.util.plusAssign

open class ZkSearchAction(
    private val onExecute: (searchText: String) -> Unit
) : ZkElement() {

    override fun onCreate() {
        classList += zkLayoutStyles.row

        + ZkTextInput(onChange = onExecute, enter = true) marginRight 8
        + ZkButton(ZkIcons.search, ZkFlavour.Primary, buttonSize = 24, iconSize = 18) {
            val value = this@ZkSearchAction[ZkTextInput::class].value
            onExecute(value)
        }
    }
}