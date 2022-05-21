/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.site.frontend.components

import zakadabar.core.browser.ZkElement
import zakadabar.core.browser.application.application
import zakadabar.core.browser.icon.ZkIcon
import zakadabar.core.browser.theme.ZkThemeRotate
import zakadabar.core.resource.ZkIcons
import zakadabar.core.resource.css.px
import zakadabar.site.frontend.DefaultLayout
import zakadabar.site.frontend.pages.Landing
import zakadabar.site.frontend.resources.SiteSuiDarkTheme
import zakadabar.site.frontend.resources.SiteSuiLightTheme
import zakadabar.site.frontend.resources.siteStyles

class SiteHeader(
    val sideBarToggle: Boolean = true
) : ZkElement() {

    override fun onCreate() {
        super.onCreate()

        + siteStyles.headerContent

        + div {
            if (sideBarToggle) {
                + siteStyles.sideBarToggle
                + ZkIcon(ZkIcons.notes)
                on("click") {
                    DefaultLayout.onToggleSideBar()
                }
            }
        }

        + div {
            + siteStyles.siteName
            + "Zakadabar"
            on("click") {
                Landing.open()
            }
        }

        + div {
            + siteStyles.version
            + application.serverDescription.version
        }

        + div {
            // placeholder in the middle
        }

        + ZkThemeRotate(
            ZkIcons.darkMode to SiteSuiDarkTheme(),
            ZkIcons.lightMode to SiteSuiLightTheme()
        ) marginRight 20.px
    }

}