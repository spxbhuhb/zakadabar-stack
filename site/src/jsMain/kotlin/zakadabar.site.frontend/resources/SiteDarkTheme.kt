/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.site.frontend.resources

import zakadabar.lib.markdown.frontend.MarkdownTheme
import zakadabar.stack.frontend.builtin.layout.zkLayoutStyles
import zakadabar.stack.frontend.builtin.theme.ZkBuiltinDarkTheme
import zakadabar.stack.frontend.builtin.titlebar.zkTitleBarStyles
import zakadabar.stack.frontend.resources.ZkColors

class SiteDarkTheme : ZkBuiltinDarkTheme(), SiteTheme {

    companion object {
        const val NAME = "site-dark"
    }

    override val name = NAME

    override val developerLogo = "/simplexion_logo.svg"

    // override var fontFamily = "system-ui, sans-serif"

    override val markdownTheme = MarkdownTheme(
        backgroundColor = ZkColors.Zakadabar.gray7,
        borderColor = borderColor,
        highlightUrl = "https://cdnjs.cloudflare.com/ajax/libs/highlight.js/10.7.2/styles/obsidian.min.css"
    )

    override fun onResume() {
        super<ZkBuiltinDarkTheme>.onResume() // FIXME remove the SiteTheme
        zkLayoutStyles.appTitleBarHeight = "60px"
        zkTitleBarStyles.appTitleBarHeight = "60px"
    }
}