/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.site.frontend.resources

import zakadabar.lib.markdown.frontend.markdownStyles
import zakadabar.stack.frontend.builtin.theme.ZkBuiltinDarkTheme

class SiteDarkTheme : ZkBuiltinDarkTheme() {

    companion object {
        const val NAME = "site-dark"
    }

    override val name = NAME

    override fun onResume() {
        super.onResume()
        with(markdownStyles) {
            codeBorderColor = borderColor
            highlightUrl = "https://cdnjs.cloudflare.com/ajax/libs/highlight.js/10.7.2/styles/obsidian.min.css"
        }
    }

}