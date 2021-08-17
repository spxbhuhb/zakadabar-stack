/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.site.frontend

import zakadabar.site.frontend.components.HeaderActions
import zakadabar.site.frontend.components.SiteLogo
import zakadabar.site.frontend.pages.Landing
import zakadabar.site.frontend.pages.ProjectStatus
import zakadabar.site.frontend.resources.siteStyles
import zakadabar.core.browser.application.application
import zakadabar.core.browser.layout.ZkDefaultLayout
import zakadabar.core.browser.titlebar.ZkAppHandle
import zakadabar.core.browser.titlebar.ZkAppTitle
import zakadabar.core.browser.titlebar.ZkAppTitleBar

object DefaultLayout : ZkDefaultLayout(spanHeader = true) {

    override fun onCreate() {
        super.onCreate()

        appHandle = ZkAppHandle(SiteLogo(), onIconClick = ::onToggleSideBar, target = Landing)
        sideBar = SideBar()

        titleBar = ZkAppTitleBar(::onToggleSideBar, fixTitle = PilotTitle())
        titleBar.globalElements += HeaderActions()

    }

    class PilotTitle : ZkAppTitle(
        application.serverDescription.name,
        contextElements = emptyList()
    ) {
        override fun onCreate() {
            + row {
                + application.serverDescription.version
                + div(siteStyles.alphaStyle) {
                    + "ALPHA"
                    on("click") {
                        application.changeNavState(ProjectStatus)
                    }
                }
            }
        }
    }
}