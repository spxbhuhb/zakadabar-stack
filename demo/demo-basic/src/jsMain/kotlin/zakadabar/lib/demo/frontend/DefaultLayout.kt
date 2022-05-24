/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.demo.frontend

import zakadabar.core.browser.application.application
import zakadabar.core.browser.theme.ZkThemeRotate
import zakadabar.core.resource.ZkIcons
import zakadabar.core.resource.css.AlignSelf
import zakadabar.core.resource.css.Display
import zakadabar.core.resource.css.px
import zakadabar.softui.browser.layout.SuiDefaultLayout
import zakadabar.softui.browser.theme.SuiDarkTheme
import zakadabar.softui.browser.theme.SuiLightTheme
import zakadabar.softui.browser.titlebar.SuiAppHeader

object DefaultLayout : SuiDefaultLayout() {

    override fun onCreate() {
        super.onCreate()

        header = SuiAppHeader(::onToggleSideBar).apply {

            globalElements += ZkThemeRotate(
                ZkIcons.darkMode to SuiDarkTheme(),
                ZkIcons.lightMode to SuiLightTheme()
            )

            titleContainer += zke {
                + Display.flex
                + AlignSelf.center

                element.style.fontSize = 14.px

                + application.serverDescription.name

                on("click") {
                    Home.open()
                }
            }
        }

        sideBar = SideBar()
    }

}