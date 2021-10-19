/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.table.header

import zakadabar.core.browser.ZkElement
import zakadabar.core.browser.button.ZkButton
import zakadabar.core.browser.input.ZkTextInput
import zakadabar.core.browser.layout.zkLayoutStyles
import zakadabar.core.browser.util.plusAssign
import zakadabar.core.resource.ZkFlavour
import zakadabar.core.resource.ZkIcons

open class ZkSearchAction(
    val onExecute: (searchText: String) -> Unit
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