/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.site.frontend.resources

import zakadabar.core.browser.form.zkFormStyles
import zakadabar.lib.markdown.browser.markdownStyles
import zakadabar.softui.browser.theme.SuiLightTheme

class SiteSuiLightTheme : SuiLightTheme() {

    companion object {
        const val NAME = "site-light"
    }

    override val name = NAME

    override fun onResume() {
        super.onResume()

        with(markdownStyles) {
            codeBorderColor = borderColor
            highlightUrl = "https://cdnjs.cloudflare.com/ajax/libs/highlight.js/10.7.2/styles/idea.min.css"
        }

        with (zkFormStyles) {
            useNativeDateInput = true
        }
    }

}