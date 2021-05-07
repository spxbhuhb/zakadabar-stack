/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.icon

import zakadabar.stack.frontend.resources.ZkTheme
import zakadabar.stack.frontend.resources.css.ZkCssStyleSheet

object ZkIconStyles : ZkCssStyleSheet<ZkTheme>() {

    val icon by cssClass {
        boxSizing = "border-box"
        width = 18 // FIXME remove hard-coded icon width and height
        height = 18
    }

    init {
        attach()
    }

}