/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.titlebar.actions

import zakadabar.stack.frontend.application.ZkApplication
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.button.ZkIconButton
import zakadabar.stack.frontend.builtin.titlebar.ZkTitleBarStyles
import zakadabar.stack.frontend.resources.ZkIcons

/**
 * An icon to switch between dark and light themes.
 */
open class DarkLightMode(
    open val darkTheme: String,
    open val lightTheme: String,
    buttonStyle: String = ZkTitleBarStyles.iconButton
) : ZkElement() {

    open val light = ZkIconButton(ZkIcons.lightMode, iconSize = 28, onClick = ::onLightMode) css buttonStyle
    open val dark = ZkIconButton(ZkIcons.darkMode, iconSize = 28, onClick = ::onDarkMode) css buttonStyle

    override fun onCreate() {
        if ("light" in ZkApplication.theme.name) {
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
        ZkApplication.theme = ZkApplication.themes.firstOrNull { it.name == lightTheme } ?: return
    }

    open fun onDarkMode() {
        dark.hide()
        light.show()
        ZkApplication.theme = ZkApplication.themes.firstOrNull { it.name == darkTheme } ?: return
    }

}