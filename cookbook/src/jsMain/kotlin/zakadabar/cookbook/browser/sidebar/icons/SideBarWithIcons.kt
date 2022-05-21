/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.cookbook.browser.sidebar.icons

import zakadabar.core.browser.sidebar.ZkSideBar
import zakadabar.core.browser.toast.toastSuccess
import zakadabar.core.resource.ZkIcons

open class SideBarWithIcons : ZkSideBar() {

    override fun onCreate() {
        super.onCreate()

        + item(ZkIcons.edit, "item 1") { toastSuccess { "Click on 1" } }

        + group(ZkIcons.globe, "group 1", onClick = { toastSuccess { "Click on group 1" }}) {
            + item(ZkIcons.account_box, "item 1.1") { toastSuccess { "Click on 1.1" } }
            + item(ZkIcons.cloudUpload, "item 1.2") { toastSuccess { "Click on 1.2" } }
        }

        + section("section 1") {
            + item(ZkIcons.edit, "item 2") { toastSuccess { "Click on 2" } }

            + group(ZkIcons.globe, "group 2", onClick = { toastSuccess { "Click on group 2" }}) {
                + item(ZkIcons.account_box, "item 2.1") { toastSuccess { "Click on 2.1" } }
                + item(ZkIcons.cloudUpload, "item 2.2") { toastSuccess { "Click on 2.2" } }
                + item("item 2.3") { toastSuccess { "Click on 2.3" } }
            }

        }

        + section(ZkIcons.account_box, "section 2") {
            + item("item 3") { toastSuccess { "Click on 3" } }

            + group("group 3", onClick = { toastSuccess { "Click on group 3" }}) {
                + item(ZkIcons.account_box, "item 3.1") { toastSuccess { "Click on 3.1" } }
                + item(ZkIcons.cloudUpload, "item 3.2") { toastSuccess { "Click on 3.2" } }
                + item("item 3.3") { toastSuccess { "Click on 3.3" } }
            }

        }
    }

}