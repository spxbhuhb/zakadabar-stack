/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.titlebar.actions

import zakadabar.core.browser.ZkElement
import zakadabar.core.browser.button.ZkButton
import zakadabar.core.browser.titlebar.zkTitleBarStyles
import zakadabar.core.resource.ZkFlavour
import zakadabar.core.resource.ZkIcons
import zakadabar.core.resource.ZkTheme
import zakadabar.core.resource.css.ZkCssStyleRule
import zakadabar.core.resource.theme

/**
 * An icon to switch between dark and light themes.
 */
@Deprecated("EOL: 2021.8.1  -  use ZkThemeRotate instead")
open class DarkLightMode(
    open val darkTheme: () -> ZkTheme,
    open val lightTheme: () -> ZkTheme,
    buttonStyle: ZkCssStyleRule = zkTitleBarStyles.iconButton
) : ZkElement() {

    open val light = ZkButton(ZkIcons.lightMode, flavour = ZkFlavour.Custom, onClick = ::onLightMode) css buttonStyle
    open val dark = ZkButton(ZkIcons.darkMode, flavour = ZkFlavour.Custom, onClick = ::onDarkMode) css buttonStyle

    // ZkIconButton(ZkIcons.lightMode, iconSize = 28, onClick = ::onLightMode) css buttonStyle

//    open val dark = ZkIconButton(ZkIcons.darkMode, iconSize = 28, onClick = ::onDarkMode) css buttonStyle

    override fun onCreate() {
        if ("light" in theme.name) {
            light.hide()
            dark.show()
        } else {
            light.show()
            dark.hide()
        }

        + light
        + dark
    }

    open fun onLightMode() {
        light.hide()
        dark.show()
        theme = lightTheme()
    }

    open fun onDarkMode() {
        dark.hide()
        light.show()
        theme = darkTheme()
    }

}