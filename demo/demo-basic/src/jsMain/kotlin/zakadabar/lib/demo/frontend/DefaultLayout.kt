/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.demo.frontend

import zakadabar.lib.demo.resources.strings
import zakadabar.core.browser.layout.ZkDefaultLayout
import zakadabar.core.browser.theme.ZkBuiltinDarkTheme
import zakadabar.core.browser.theme.ZkBuiltinLightTheme
import zakadabar.core.browser.theme.ZkGreenBlueTheme
import zakadabar.core.browser.theme.ZkThemeRotate
import zakadabar.core.browser.titlebar.ZkAppHandle
import zakadabar.core.browser.titlebar.ZkAppTitleBar
import zakadabar.core.resource.ZkIcons

object DefaultLayout : ZkDefaultLayout(spanHeader = false) {

    override fun onCreate() {
        super.onCreate()

        appHandle = ZkAppHandle(zke { + strings.home }, onIconClick = ::onToggleSideBar, target = Home)
        sideBar = SideBar()

        titleBar = ZkAppTitleBar(::onToggleSideBar)

        titleBar.globalElements += ZkThemeRotate(
            ZkIcons.darkMode to ZkBuiltinDarkTheme(),
            ZkIcons.lightMode to ZkBuiltinLightTheme(),
            ZkIcons.leaf to ZkGreenBlueTheme()
        )
    }

}