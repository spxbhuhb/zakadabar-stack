/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.titlebar.actions

import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.button.ZkButton
import zakadabar.stack.frontend.builtin.titlebar.zkTitleBarStyles
import zakadabar.stack.frontend.resources.ZkFlavour
import zakadabar.stack.frontend.resources.ZkIcons
import zakadabar.stack.frontend.resources.ZkTheme
import zakadabar.stack.frontend.resources.theme

/**
 * An icon to switch between dark and light themes.
 */
open class DarkLightMode(
    open val darkTheme: () -> ZkTheme,
    open val lightTheme: () -> ZkTheme,
    buttonStyle: String = zkTitleBarStyles.iconButton
) : ZkElement() {

    open val light = ZkButton(ZkIcons.lightMode, flavour = ZkFlavour.Custom, iconSize = 28, onClick = ::onLightMode) css buttonStyle
    open val dark = ZkButton(ZkIcons.darkMode, flavour = ZkFlavour.Custom, iconSize = 28, onClick = ::onDarkMode) css buttonStyle

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