/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.site.frontend

import zakadabar.site.frontend.components.SiteLogo
import zakadabar.site.frontend.pages.misc.Landing
import zakadabar.site.frontend.resources.SiteStyles
import zakadabar.stack.frontend.builtin.layout.ZkDefaultLayout
import zakadabar.stack.frontend.builtin.titlebar.ZkAppHandle
import zakadabar.stack.frontend.builtin.titlebar.ZkAppTitleBar

object DefaultLayout : ZkDefaultLayout() {

    override fun onCreate() {
        super.onCreate()

        appHandle = ZkAppHandle(SiteLogo(), onIconClick = ::onToggleSideBar, onTextClick = { Landing.open() })
        sideBar = SideBar
        titleBar = ZkAppTitleBar(::onToggleSideBar)

    }

}