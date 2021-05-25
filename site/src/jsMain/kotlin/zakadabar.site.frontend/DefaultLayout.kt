/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.site.frontend

import zakadabar.site.frontend.components.HeaderActions
import zakadabar.site.frontend.components.SiteLogo
import zakadabar.site.frontend.pages.Landing
import zakadabar.stack.frontend.application.application
import zakadabar.stack.frontend.builtin.layout.ZkDefaultLayout
import zakadabar.stack.frontend.builtin.titlebar.ZkAppHandle
import zakadabar.stack.frontend.builtin.titlebar.ZkAppTitle
import zakadabar.stack.frontend.builtin.titlebar.ZkAppTitleBar

object DefaultLayout : ZkDefaultLayout(spanHeader = true) {

    override fun onCreate() {
        super.onCreate()

        appHandle = ZkAppHandle(SiteLogo(), onIconClick = ::onToggleSideBar, target = Landing)
        sideBar = SideBar()

        titleBar = ZkAppTitleBar(::onToggleSideBar, fixTitle = PilotTitle())
        titleBar.globalElements += HeaderActions()

    }

    class PilotTitle : ZkAppTitle(
        application.serverDescription.version,
        contextElements = emptyList()
    )
}