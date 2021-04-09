/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package zakadabar.site.frontend

import zakadabar.site.frontend.pages.misc.ChangeLog
import zakadabar.site.frontend.pages.misc.Content
import zakadabar.site.frontend.pages.misc.Home
import zakadabar.stack.frontend.application.ZkAppRouting

object Routing : ZkAppRouting(DefaultLayout, Home) {

    init {
        + Home
        + ChangeLog
        + Content
    }

}