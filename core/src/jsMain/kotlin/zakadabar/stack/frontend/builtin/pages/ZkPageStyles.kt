/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.pages

import zakadabar.stack.frontend.resources.css.ZkCssStyleSheet

object ZkPageStyles : ZkCssStyleSheet() {

    val page by cssClass {
        width = "100%"
        height = "100%"
        margin = theme.layout.marginStep * 2
        backgroundColor = theme.layout.defaultBackground
    }

    init {
        attach()
    }
}