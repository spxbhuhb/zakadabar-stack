/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.cookbook.browser.sidebar.icons

import zakadabar.core.browser.sidebar.ZkSideBar
import zakadabar.core.browser.toast.toastSuccess
import zakadabar.core.resource.ZkIcons

class SideBarWithIcons : ZkSideBar() {

    override fun onCreate() {
        super.onCreate()

        + item(ZkIcons.edit, "item 1") { toastSuccess { "Click on 1" } }

        + group(ZkIcons.globe, "group 1") {
            + item(ZkIcons.account_box, "item 1.1") { toastSuccess { "Click on 1.1" } }
            + item(ZkIcons.cloudUpload, "item 1.2") { toastSuccess { "Click on 1.2" } }
        }

    }

}