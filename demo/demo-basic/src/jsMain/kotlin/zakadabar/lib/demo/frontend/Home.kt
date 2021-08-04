/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.demo.frontend

import zakadabar.lib.demo.resources.strings
import zakadabar.stack.frontend.builtin.layout.zkLayoutStyles
import zakadabar.stack.frontend.builtin.pages.ZkPage

object Home : ZkPage() {

    override fun onCreate() {
        + div(zkLayoutStyles.p1) {
            + strings.home
        }
    }

}