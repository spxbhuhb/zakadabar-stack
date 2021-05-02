/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.site.frontend.resources

import zakadabar.stack.frontend.builtin.theme.ZkBuiltinDarkTheme

class SiteDarkTheme : ZkBuiltinDarkTheme(), SiteTheme {

    companion object {
        const val NAME = "site-dark"
    }

    override val name = NAME

    override val developerLogo = "/simplexion_logo.svg"

    init {
        titleBar.height = "60px"
    }
}