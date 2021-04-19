/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.pages

import zakadabar.stack.frontend.resources.ZkTheme
import zakadabar.stack.frontend.resources.css.ZkCssStyleSheet

object ZkPageStyles : ZkCssStyleSheet<ZkTheme>() {

    val page by cssClass {
        margin = theme.layout.marginStep * 2
    }

    init {
        attach()
    }
}