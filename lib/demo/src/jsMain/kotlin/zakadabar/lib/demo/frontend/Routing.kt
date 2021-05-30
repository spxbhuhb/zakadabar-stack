/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package zakadabar.lib.demo.frontend

import zakadabar.stack.frontend.application.ZkAppRouting

class Routing : ZkAppRouting(DefaultLayout, Home) {

    init {
        + Home
        zakadabar.lib.accounts.frontend.install(this)
    }

}