/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.frontend.pages.misc

import zakadabar.demo.resources.Strings
import zakadabar.stack.frontend.builtin.pages.ZkPage
import zakadabar.stack.frontend.builtin.titlebar.ZkAppTitle

object Home : ZkPage() {

    override fun onCreate() {
        super.onCreate()
        + Strings.home
    }

}