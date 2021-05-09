/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.site.frontend.resources

import zakadabar.lib.markdown.frontend.MarkdownTheme
import zakadabar.stack.frontend.builtin.theme.ZkBuiltinLightTheme

class SiteLightTheme : ZkBuiltinLightTheme(), SiteTheme {

    companion object {
        const val NAME = "site-light"
    }

    override val name = NAME

    override val developerLogo = "/simplexion_logo.svg"

    // override var fontFamily = "system-ui, sans-serif"

    override val markdownTheme = MarkdownTheme(
        backgroundColor = "#f5f7f9",
        borderColor = border
    )

    init {
        titleBar.appTitleBarHeight = "60px"
    }
}