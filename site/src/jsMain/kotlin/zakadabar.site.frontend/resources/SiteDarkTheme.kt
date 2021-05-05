/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.site.frontend.resources

import zakadabar.lib.frontend.markdown.MarkdownTheme
import zakadabar.stack.frontend.builtin.theme.ZkBuiltinDarkTheme
import zakadabar.stack.frontend.resources.ZkColors

class SiteDarkTheme : ZkBuiltinDarkTheme(), SiteTheme {

    companion object {
        const val NAME = "site-dark"
    }

    override val name = NAME

    override val developerLogo = "/simplexion_logo.svg"

    override val markdownTheme = MarkdownTheme(
        backgroundColor = ZkColors.Design.gray7,
        borderColor = color.border,
        highlightUrl = "https://cdnjs.cloudflare.com/ajax/libs/highlight.js/10.7.2/styles/obsidian.min.css"
    )

    init {
        titleBar.height = "60px"
    }
}