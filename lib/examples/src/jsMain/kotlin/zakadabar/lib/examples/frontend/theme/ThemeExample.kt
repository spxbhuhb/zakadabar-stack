/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.examples.frontend.theme

import org.w3c.dom.HTMLElement
import zakadabar.lib.examples.resources.strings
import zakadabar.core.browser.ZkElement
import zakadabar.core.browser.button.ZkButton
import zakadabar.core.browser.sidebar.zkSideBarStyles
import zakadabar.core.browser.theme.ZkBuiltinLightTheme
import zakadabar.core.browser.titlebar.zkTitleBarStyles
import zakadabar.core.resource.ZkColors
import zakadabar.core.resource.css.px
import zakadabar.core.resource.theme
import zakadabar.core.browser.util.marginBottom

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
            appTitleBarBackground = ZkColors.Green.c500
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
            gridGap = 10.px

            + ZkButton(strings.green) { theme = ExampleThemeGreen() }
            + ZkButton(strings.red) { theme = ExampleThemeRed() }

        } marginBottom 20

    }

}