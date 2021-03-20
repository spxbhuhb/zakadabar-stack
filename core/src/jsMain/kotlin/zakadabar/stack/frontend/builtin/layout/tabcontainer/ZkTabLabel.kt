/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.layout.tabcontainer

import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.util.minusAssign
import zakadabar.stack.frontend.util.plusAssign

class ZkTabLabel(
    private val container: ZkTabContainer,
    val item: TabItem
) : ZkElement() {

    var active = false
        set(value) {
            if (value) {
                classList += ZkTabContainerStyles.activeLabel
            } else {
                classList -= ZkTabContainerStyles.activeLabel
            }
            field = value
        }

    override fun onCreate() {
        super.onCreate()
        className = ZkTabContainerStyles.label

        + item.title

        on("click") {
            container.switchTo(item)
        }
    }
}
