/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.cookbook.browser.sidebar.icons


import zakadabar.core.browser.icon.ZkNotificationIcon
import zakadabar.core.browser.sidebar.ZkSideBar
import zakadabar.core.browser.toast.toastSuccess
import zakadabar.core.resource.ZkIcons

class SideBarWithNotificationIcons : ZkSideBar() {

    val icon1 = ZkIcons.cloudUpload
    val icon2 = ZkIcons.checkCircle
    val ni1 = ZkNotificationIcon(icon1)
    val ni2 = ZkNotificationIcon(icon2)

    override fun onCreate() {
        super.onCreate()

        + itemWithNotification(
            text = "Title 1",
            ni = ni1
        ) { toastSuccess { "Click on 1" } }

        ni1.redrawIcon(2)

        + itemWithNotification(
            text = "Title 2",
            ni = ni2
        ) { toastSuccess { "Click on 2" } }

        ni2.redrawIcon(19)
    }

}