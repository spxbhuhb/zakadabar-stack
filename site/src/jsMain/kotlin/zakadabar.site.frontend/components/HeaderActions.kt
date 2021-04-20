/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.site.frontend.components

import zakadabar.site.frontend.resources.SiteDarkTheme
import zakadabar.site.frontend.resources.SiteLightTheme
import zakadabar.site.frontend.resources.SiteStyles
import zakadabar.stack.frontend.application.ZkApplication
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.button.ZkIconButton
import zakadabar.stack.frontend.resources.ZkIcons
import zakadabar.stack.frontend.util.marginRight

class HeaderActions : ZkElement() {

    val light = ZkIconButton(ZkIcons.lightMode, iconSize = 28, onClick = ::onLightMode) css SiteStyles.headerButton
    val dark = ZkIconButton(ZkIcons.darkMode, iconSize = 28, onClick = ::onDarkMode) css SiteStyles.headerButton

    override fun onCreate() {
        if ("light" in ZkApplication.theme.name) {
            light.hide()
            dark.show()
        } else {
            light.show()
            dark.hide()
        }

        + row(SiteStyles.headerActions) {
            + light
            + dark
        } marginRight 20
    }

    private fun onLightMode() {
        light.hide()
        dark.show()
        ZkApplication.theme = SiteLightTheme()
    }

    private fun onDarkMode() {
        dark.hide()
        light.show()
        ZkApplication.theme = SiteDarkTheme()
    }

}