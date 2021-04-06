/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.site.frontend

import zakadabar.stack.frontend.builtin.sidebar.ZkSideBar

object SideBar : ZkSideBar() {

    init {
        style {
            height = "100%"
            backgroundImage = """url("/menu_background.jpg")"""
            backgroundSize = "cover"
        }
    }

}



