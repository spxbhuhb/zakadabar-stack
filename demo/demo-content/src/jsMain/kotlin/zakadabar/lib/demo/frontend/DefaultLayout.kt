/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.demo.frontend

import zakadabar.lib.demo.resources.strings
import zakadabar.stack.frontend.builtin.layout.ZkDefaultLayout
import zakadabar.stack.frontend.builtin.theme.ZkBuiltinDarkTheme
import zakadabar.stack.frontend.builtin.theme.ZkBuiltinLightTheme
import zakadabar.stack.frontend.builtin.theme.ZkGreenBlueTheme
import zakadabar.stack.frontend.builtin.theme.ZkThemeRotate
import zakadabar.stack.frontend.builtin.titlebar.ZkAppHandle
import zakadabar.stack.frontend.builtin.titlebar.ZkAppTitleBar
import zakadabar.stack.frontend.resources.ZkIcons

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