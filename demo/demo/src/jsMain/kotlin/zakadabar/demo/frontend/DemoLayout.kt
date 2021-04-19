/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.frontend

import zakadabar.demo.resources.Strings
import zakadabar.stack.frontend.builtin.layout.ZkDefaultLayout
import zakadabar.stack.frontend.builtin.misc.ZkAppHandle
import zakadabar.stack.frontend.builtin.sidebar.ZkSideBarStyles
import zakadabar.stack.frontend.builtin.titlebar.ZkAppTitleBar
import zakadabar.stack.frontend.util.asElement

object DemoLayout : ZkDefaultLayout() {

    override fun onCreate() {
        super.onCreate()

        appHandle = ZkAppHandle(Strings.applicationName.asElement(), onIconClick = ::onToggleSideBar)
        sideBar = SideBar
        titleBar = ZkAppTitleBar(::onToggleSideBar)

    }

}
