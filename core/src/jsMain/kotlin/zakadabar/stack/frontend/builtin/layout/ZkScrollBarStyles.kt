/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.layout

import zakadabar.stack.frontend.resources.ZkTheme
import zakadabar.stack.frontend.resources.css.ZkCssStyleSheet

var scrollBarStyles = ZkScrollBarStyles()

class ZkScrollBarStyles : ZkCssStyleSheet<ZkTheme>() {

    val webkitScrollBar by cssRule("::-webkit-scrollbar") {
        width = theme.scrollBar.width
        height = theme.scrollBar.height
    }

    val webkitScrollBarThumb by cssRule("::-webkit-scrollbar-thumb") {
        backgroundColor = theme.scrollBar.background
    }

    val webkitScrollBarTrack by cssRule("::-webkit-scrollbar-track") {
        backgroundColor = theme.scrollBar.foreground
    }

    init {
        attach()
    }

}