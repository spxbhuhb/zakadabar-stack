/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.site.frontend

import zakadabar.site.frontend.components.SiteLogo
import zakadabar.site.frontend.pages.Landing
import zakadabar.site.frontend.pages.Roadmap
import zakadabar.site.frontend.resources.SiteDarkTheme
import zakadabar.site.frontend.resources.SiteLightTheme
import zakadabar.stack.frontend.application.application
import zakadabar.stack.frontend.builtin.button.ZkButton
import zakadabar.stack.frontend.builtin.layout.ZkDefaultLayout
import zakadabar.stack.frontend.builtin.titlebar.ZkAppHandle
import zakadabar.stack.frontend.builtin.titlebar.ZkAppTitle
import zakadabar.stack.frontend.builtin.titlebar.ZkAppTitleBar
import zakadabar.stack.frontend.builtin.titlebar.actions.DarkLightMode
import zakadabar.stack.frontend.resources.ZkFlavour

object DefaultLayout : ZkDefaultLayout(spanHeader = true) {

    override fun onCreate() {
        super.onCreate()

        appHandle = ZkAppHandle(SiteLogo(), onIconClick = ::onToggleSideBar, target = Landing)
        sideBar = SideBar()

        titleBar = ZkAppTitleBar(::onToggleSideBar, fixTitle = PilotTitle())
        titleBar.globalElements += DarkLightMode({ SiteDarkTheme() }, { SiteLightTheme() })

    }

    class PilotTitle : ZkAppTitle(
        application.serverDescription.version,
        contextElements = listOf(
            ZkButton("zakadabar.io runs in pilot, click here for details", ZkFlavour.Warning, capitalize = false) {
                application.changeNavState(Roadmap)
            }
        )
    )
}