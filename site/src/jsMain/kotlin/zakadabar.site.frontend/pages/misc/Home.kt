/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.site.frontend.pages.misc

import zakadabar.stack.frontend.builtin.pages.ZkPage

object Home : ZkPage() {

    override fun onCreate() {
        style {
            overflowY = "scroll"
            padding = "20px"
        }
    }

}