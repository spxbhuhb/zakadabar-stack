/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.softui.browser.titlebar

import zakadabar.core.browser.button.ZkButton
import zakadabar.core.browser.titlebar.ZkAppTitleBar
import zakadabar.core.browser.titlebar.zkTitleBarStyles
import zakadabar.core.browser.util.plusAssign
import zakadabar.core.resource.ZkFlavour
import zakadabar.core.resource.ZkIcons
import zakadabar.softui.browser.theme.styles.suiTitleBarStyles

open class SuiAppHeader(
    onToggleSideBar: () -> Unit = { },
) : ZkAppTitleBar(onToggleSideBar) {

    override fun onCreate() {
        classList += suiTitleBarStyles.appHeader

        + handleContainer css zkTitleBarStyles.sidebarHandle build {
            + ZkButton(ZkIcons.notes, flavour = ZkFlavour.Custom, onClick = ::onHandleClick)
        }

        + titleContainer css zkTitleBarStyles.titleContainer marginRight 10

        + globalElements css zkTitleBarStyles.globalElementContainer marginRight 10
    }

}