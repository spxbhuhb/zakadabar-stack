/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.site.frontend

import zakadabar.stack.frontend.application.ZkApplication
import zakadabar.stack.frontend.resources.css.ZkCssStyleSheet

object SiteStyles : ZkCssStyleSheet<SiteStyles>(ZkApplication.theme) {

    val page by cssClass {
        overflowY = "scroll"
        padding = 20
    }

    init {
        attach()
    }
}