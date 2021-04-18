/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.site.frontend

import zakadabar.site.frontend.components.Logo
import zakadabar.site.frontend.resources.SiteStyles
import zakadabar.stack.frontend.builtin.layout.ZkDefaultLayout
import zakadabar.stack.frontend.builtin.misc.ZkAppHandle
import zakadabar.stack.frontend.builtin.titlebar.ZkAppTitleBar

object DefaultLayout : ZkDefaultLayout() {

    override fun onCreate() {
        super.onCreate()

        appHandle = ZkAppHandle(Logo(), onIconClick = ::onToggleSideBar)
        sideBar = SideBar
        titleBar = ZkAppTitleBar(::onToggleSideBar)

        appHandle css SiteStyles.header
        titleBar css SiteStyles.header
    }

}