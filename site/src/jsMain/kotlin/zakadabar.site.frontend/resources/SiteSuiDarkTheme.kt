/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.site.frontend.resources

import zakadabar.core.browser.titlebar.zkTitleBarStyles
import zakadabar.lib.markdown.browser.markdownStyles
import zakadabar.softui.browser.theme.SoftUiDarkTheme
import zakadabar.softui.browser.theme.base.Colors
import zakadabar.softui.browser.theme.styles.suiLayoutStyles

class SiteSuiDarkTheme : SoftUiDarkTheme() {

    companion object {
        const val NAME = "site-dark"
    }

    override val name = NAME

    override fun onResume() {
        super.onResume()

        with(suiLayoutStyles) {
            headerBackground = backgroundColor
        }

        with(zkTitleBarStyles) {
            appHandleText = Colors.white.main
            appTitleBarText = Colors.white.main
        }

        with(markdownStyles) {
            codeBorderColor = borderColor
            highlightUrl = "https://cdnjs.cloudflare.com/ajax/libs/highlight.js/10.7.2/styles/obsidian.min.css"
        }
    }

}