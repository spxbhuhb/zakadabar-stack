/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.demo.frontend.resources

import zakadabar.core.browser.sidebar.zkSideBarStyles
import zakadabar.core.browser.theme.ZkBuiltinLightTheme
import zakadabar.core.browser.titlebar.zkTitleBarStyles

class AppLightTheme : ZkBuiltinLightTheme() {

    companion object {
        const val NAME = "app-light"
    }

    override val name = NAME

    // -------------------------------------------------------------------------
    // Customize theme variables
    // -------------------------------------------------------------------------

    override var boxShadow = "0 2px 2px 0 rgba(0, 0, 0, 0.14), 0 3px 1px -2px rgba(0, 0, 0, 0.2), 0 1px 5px 0 rgba(0, 0, 0, 0.12)"

    // -------------------------------------------------------------------------
    // Customize style variables
    // -------------------------------------------------------------------------

    override fun onResume() {
        super.onResume()

        with(zkTitleBarStyles) {
            appHandleBackground = "rgba(8,14,53,1)"
            appHandleText = "white"

            appTitleBarBackground = "rgba(8,14,53,1)"
            appTitleBarText = "white"

            iconButton

        }

        with(zkSideBarStyles) {
            backgroundColor = "rgba(8,14,100,0.5)"
            textColor = "white"
        }
    }

}