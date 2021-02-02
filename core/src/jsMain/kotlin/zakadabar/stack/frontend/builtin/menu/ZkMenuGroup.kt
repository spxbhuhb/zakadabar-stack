/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.menu

import zakadabar.stack.frontend.builtin.icon.ZkIcon
import zakadabar.stack.frontend.elements.ZkElement
import zakadabar.stack.frontend.resources.Icons

class ZkMenuGroup(
    private val text: String,
    builder: ZkElement.() -> Unit
) : ZkElement() {

    private var open = false
    private val openIcon = ZkIcon(Icons.arrowDropDown)
    private val closeIcon = ZkIcon(Icons.arrowDropUp)

    // TODO think about init block vs init function

    init {
        + column {
            + div(ZkMenuStyles.groupTitle) {
                + text
                + row {
                    + openIcon
                    + closeIcon.hide()
                }
                on(buildContext, "click") { _ -> if (open) onClose() else onOpen() }
            }
            + zke(ZkMenuStyles.groupContent) {
                hide()
                this.builder()
            }
        }
    }

    private fun onOpen() {
        get<ZkElement>(ZkMenuStyles.groupContent).show()
        openIcon.hide()
        closeIcon.show()
        open = true
    }

    private fun onClose() {
        get<ZkElement>(ZkMenuStyles.groupContent).hide()
        closeIcon.hide()
        openIcon.show()
        open = false
    }
}