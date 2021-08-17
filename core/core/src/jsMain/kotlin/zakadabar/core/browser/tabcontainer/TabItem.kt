/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.tabcontainer

import zakadabar.core.browser.ZkElement

open class TabItem(
    container: ZkTabContainer,
    val content: ZkElement,
    val title: String? = null,
    label: ZkTabLabel? = null
) {
    val label: ZkTabLabel = label ?: ZkTabLabel(container, this)
}