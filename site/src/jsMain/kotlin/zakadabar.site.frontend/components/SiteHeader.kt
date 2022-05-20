/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.site.frontend.components

import zakadabar.core.browser.ZkElement
import zakadabar.core.browser.application.application
import zakadabar.core.browser.theme.ZkThemeRotate
import zakadabar.core.resource.ZkIcons
import zakadabar.core.resource.css.px
import zakadabar.site.frontend.resources.SiteSuiDarkTheme
import zakadabar.site.frontend.resources.SiteSuiLightTheme
import zakadabar.site.frontend.resources.siteStyles

class SiteHeader : ZkElement() {

    override fun onCreate() {
        super.onCreate()

        + siteStyles.headerContent

        + div {
            + siteStyles.siteName
            + "Zakadabar"
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