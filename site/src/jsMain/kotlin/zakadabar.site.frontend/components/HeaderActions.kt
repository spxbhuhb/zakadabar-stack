/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.site.frontend.components

import zakadabar.core.browser.ZkElement
import zakadabar.core.browser.theme.ZkThemeRotate
import zakadabar.core.resource.ZkIcons
import zakadabar.site.frontend.resources.SiteSuiDarkTheme
import zakadabar.site.frontend.resources.SiteSuiLightTheme

class HeaderActions : ZkElement() {

    override fun onCreate() {
        + ZkThemeRotate(
            ZkIcons.darkMode to SiteSuiDarkTheme(),
            ZkIcons.lightMode to SiteSuiLightTheme()
        )
    }

}