/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.site.frontend.resources

import zakadabar.stack.frontend.resources.css.ZkCssStyleSheet

object SiteStyles : ZkCssStyleSheet<SiteTheme>() {

    val logo by cssClass {
        cursor = "pointer"
        height = 22
        fill = theme.layout.defaultForeground
    }

    val headerActions by cssClass {
        alignItems = "center"
    }

    val developerLogo by cssClass {
        width = 48
        height = 48
        fill = theme.layout.defaultForeground
    }

    init {
        attach()
    }
}