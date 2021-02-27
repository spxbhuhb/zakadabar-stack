/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.sidebar

import zakadabar.stack.frontend.builtin.icon.ZkIcon
import zakadabar.stack.frontend.elements.ZkElement
import zakadabar.stack.frontend.resources.Icons

class ZkSideBarGroup(
    private val text: String,
    builder: ZkElement.() -> Unit
) : ZkElement() {

    private var open = false
    private val openIcon = ZkIcon(Icons.arrowDropDown)
    private val closeIcon = ZkIcon(Icons.arrowDropUp)

    // TODO think about init block vs init function

    init {
        + column {
            + div(ZkSideBarStyles.groupTitle) {
                + text
                + row {
                    + openIcon
                    + closeIcon.hide()
                }
                on(currentElement, "click") { _ -> if (open) onClose() else onOpen() }
            }
            + zke(ZkSideBarStyles.groupContent) {
                hide()
                this.builder()
            }
        }
    }

    private fun onOpen() {
        get<ZkElement>(ZkSideBarStyles.groupContent).show()
        openIcon.hide()
        closeIcon.show()
        open = true
    }

    private fun onClose() {
        get<ZkElement>(ZkSideBarStyles.groupContent).hide()
        closeIcon.hide()
        openIcon.show()
        open = false
    }
}