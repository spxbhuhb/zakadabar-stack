/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.examples.frontend.theme

import org.w3c.dom.HTMLElement
import zakadabar.lib.examples.resources.strings
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.button.ZkButton
import zakadabar.stack.frontend.builtin.sidebar.zkSideBarStyles
import zakadabar.stack.frontend.builtin.theme.ZkBuiltinLightTheme
import zakadabar.stack.frontend.builtin.titlebar.zkTitleBarStyles
import zakadabar.stack.frontend.resources.ZkColors
import zakadabar.stack.frontend.resources.theme
import zakadabar.stack.frontend.util.marginBottom

class ExampleThemeRed : ZkBuiltinLightTheme() {

    override var name = "example-red"

    override var primaryColor = ZkColors.Red.c500

    override fun onResume() {
        super.onResume() // apply defaults from built-in light theme

        zkSideBarStyles.backgroundColor = ZkColors.Red.c500

        with(zkTitleBarStyles) {
            appHandleBackground = ZkColors.Red.c500
        }

    }
}

class ExampleThemeGreen : ZkBuiltinLightTheme() {

    override var name = "example-green"

    override var primaryColor = ZkColors.Green.c500

    override fun onResume() {
        super.onResume()  // apply defaults from built-in light theme

        with(zkTitleBarStyles) {
            appHandleBackground = ZkColors.Green.c500
            titleBarBackground = ZkColors.Green.c500
        }

    }
}

/**
 * This example shows how to write and switch themes
 */
class ThemeExample(
    element: HTMLElement
) : ZkElement(element) {

    override fun onCreate() {
        super.onCreate()

        + grid {

            gridTemplateColumns = "repeat(4, max-content)"
            gridGap = 10

            + ZkButton(strings.green) { theme = ExampleThemeGreen() }
            + ZkButton(strings.red) { theme = ExampleThemeRed() }

        } marginBottom 20

    }

}