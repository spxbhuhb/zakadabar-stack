/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.site.frontend.resources

import zakadabar.lib.markdown.frontend.MarkdownTheme
import zakadabar.stack.frontend.builtin.layout.zkLayoutStyles
import zakadabar.stack.frontend.builtin.theme.ZkBuiltinLightTheme
import zakadabar.stack.frontend.builtin.titlebar.zkTitleBarStyles

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

    override fun onResume() {
        super<ZkBuiltinLightTheme>.onResume() // FIXME remove the SiteTheme
        zkLayoutStyles.appTitleBarHeight = "60px"
        zkTitleBarStyles.appTitleBarHeight = "60px"
    }
}