/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.icon

import zakadabar.core.browser.ZkElement
import zakadabar.core.browser.sidebar.zkSideBarStyles
import zakadabar.core.resource.ZkIconSource

open class ZkNotificationIcon(
    val icon: ZkIconSource,
    val iconSize: Int = 18,
) : ZkElement() {

    val iconElement =  ZkIcon(icon, size = iconSize)

    override fun onCreate() {
        super.onCreate()

        + zkIconStyles.notificationIconContainer

        + iconElement css zkSideBarStyles.icon
    }

    /**
     * call [redrawIcon] to change the number in the counter dot
     * @param count will be the number in the dot, if it is 0, the dot won't show up
     * */
    open fun redrawIcon(count: Int) {
        iconElement -= ZkNotificationCountDot::class
        iconElement += ZkNotificationCountDot(count) css zkIconStyles.notificationIconStyle
    }
}
