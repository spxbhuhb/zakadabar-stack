/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.marina.frontend.pages

import zakadabar.demo.marina.resources.Strings
import zakadabar.stack.frontend.builtin.pages.ZkPage

object Home : ZkPage() {

    override fun onCreate() {
        super.onCreate()
        + Strings.home
    }

}