/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.sidebar

import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.icon.ZkIcon
import zakadabar.stack.frontend.resources.ZkIcons

/**
 * A group of sidebar entries. Supports open and close.
 *
 * @property  onClick  A function to call when the user clicks on the group title. The parameter shows
 *                     if the bar is open or close. True means that the user opened a bar just now,
 *                     false means that the user closed it.
 */
class ZkSideBarGroup(
    private val text: String,
    private var onClick: (Boolean) -> Unit = { },
    private val builder: ZkElement.() -> Unit
) : ZkElement() {

    private var open = false
    private val openIcon = ZkIcon(ZkIcons.arrowRight, 18)
    private val closeIcon = ZkIcon(ZkIcons.arrowDropDown, 18)

    override fun onCreate() {
        + column {
            + div(ZkSideBarStyles.groupTitle) {
                + openIcon
                + closeIcon.hide()
                + text
                on(buildElement, "click") { _ -> if (open) onClose() else onOpen() }
            }
            + zke(ZkSideBarStyles.groupContent) {
                hide()
                builder()
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