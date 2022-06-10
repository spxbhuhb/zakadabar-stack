/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.site.frontend.components

import zakadabar.core.browser.application.application
import zakadabar.core.browser.theme.ZkThemeRotate
import zakadabar.core.browser.util.marginRight
import zakadabar.core.resource.ZkIcons
import zakadabar.core.resource.css.AlignSelf
import zakadabar.core.resource.css.Display
import zakadabar.core.resource.css.px
import zakadabar.site.frontend.pages.Landing
import zakadabar.softui.browser.theme.SuiDarkTheme
import zakadabar.softui.browser.theme.SuiLightTheme
import zakadabar.softui.browser.titlebar.SuiAppHeader

class SiteHeader(
    onToggleSideBar: () -> Unit = { },
) : SuiAppHeader(onToggleSideBar) {

    override fun onCreate() {
        super.onCreate()

        titleContainer += zke {
            + Display.flex
            + AlignSelf.center

            element.style.fontSize = 14.px

            + div { + "Zakadabar" } marginRight 20.px
            + application.serverDescription.version

            on("click") {
                Landing.open()
            }
        }

        globalElements += ZkThemeRotate(
            ZkIcons.darkMode to SuiDarkTheme(),
            ZkIcons.lightMode to SuiLightTheme()
        )
    }

}