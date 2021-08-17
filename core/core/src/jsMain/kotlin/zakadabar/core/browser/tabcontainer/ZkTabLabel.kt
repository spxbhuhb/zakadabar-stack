/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.tabcontainer

import zakadabar.core.browser.ZkElement
import zakadabar.core.browser.util.minusAssign
import zakadabar.core.browser.util.plusAssign

open class ZkTabLabel(
    open val container: ZkTabContainer,
    open val item: TabItem
) : ZkElement() {

    open var active = false
        set(value) {
            if (value) {
                classList += zkTabContainerStyles.activeLabel
            } else {
                classList -= zkTabContainerStyles.activeLabel
            }
            field = value
        }

    override fun onCreate() {
        super.onCreate()

        + zkTabContainerStyles.label

        + item.title

        on("click") {
            container.switchTo(item)
        }
    }
}
