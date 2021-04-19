/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.site.frontend.resources

import zakadabar.stack.frontend.builtin.theme.ZkBuiltinLightTheme

class SiteLightTheme : ZkBuiltinLightTheme(), SiteTheme {

    override val developerLogo = "/simplexion_logo.svg"

    init {
        layout = layout.copy(
            titleBarHeight = 60
        )
    }
}