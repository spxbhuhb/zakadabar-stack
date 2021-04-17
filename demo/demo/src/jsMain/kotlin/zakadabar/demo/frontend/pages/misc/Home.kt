/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.frontend.pages.misc

import zakadabar.demo.resources.Strings
import zakadabar.stack.frontend.builtin.pages.ZkPage

object Home : ZkPage(title = Strings.home) {

    override fun onCreate() {
        super.onCreate()
        + Strings.home
    }

}