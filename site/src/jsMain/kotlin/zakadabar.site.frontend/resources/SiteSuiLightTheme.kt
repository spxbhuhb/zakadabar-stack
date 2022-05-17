/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.site.frontend.resources

import zakadabar.lib.markdown.browser.markdownStyles
import zakadabar.softui.browser.theme.SoftUiLightTheme
import zakadabar.softui.browser.theme.base.Colors

class SiteSuiLightTheme : SoftUiLightTheme() {

    companion object {
        const val NAME = "site-light"
    }

    override val name = NAME

    override var backgroundColor = Colors.grey.g100

    override fun onResume() {
        super.onResume()

        with(markdownStyles) {
            codeBorderColor = borderColor
            highlightUrl = "https://cdnjs.cloudflare.com/ajax/libs/highlight.js/10.7.2/styles/idea.min.css"
        }
    }

}