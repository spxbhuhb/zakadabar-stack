/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.site.frontend.resources

import zakadabar.stack.frontend.builtin.theme.ZkBuiltinDarkTheme

class SiteDarkTheme : ZkBuiltinDarkTheme(), SiteTheme {

    override val developerLogo = "url(/simplexion_logo_dark.png)"

    init {
        layout = layout.copy(
            titleBarHeight = 60
        )
    }
}