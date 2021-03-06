/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.frontend

import zakadabar.stack.frontend.application.AppLayout
import zakadabar.stack.frontend.builtin.layout.ZkLayoutStyles
import zakadabar.stack.frontend.util.plusAssign

object DefaultLayout : AppLayout("default") {

    override fun onCreate() {
        classList += ZkLayoutStyles.h100
        classList += ZkLayoutStyles.row

        + SideBar
        + content withCss ZkLayoutStyles.layoutContent
    }

}