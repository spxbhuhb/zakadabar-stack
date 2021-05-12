/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.layout

import zakadabar.stack.frontend.resources.ZkTheme
import zakadabar.stack.frontend.resources.css.ZkCssStyleSheet
import zakadabar.stack.frontend.resources.css.cssStyleSheet

val zkLayoutStyles by cssStyleSheet(ZkLayoutStyles())

class ZkLayoutStyles : ZkCssStyleSheet<ZkTheme>() {

    val w100 by cssClass {
        width = "100%"
    }

    val h100 by cssClass {
        height = "100%"
    }

    val hidden by cssClass {
        display = "none !important"
    }

    val row by cssClass {
        display = "flex"
        flexDirection = "row"
    }

    val column by cssClass {
        display = "flex"
        flexDirection = "column"
    }

    val grow by cssClass {
        flexGrow = 1
    }

    val grid by cssClass {
        display = "grid"
        borderCollapse = "collapse"
        border = 0
    }

    val border by cssClass {
        border = theme.border
    }

    val block by cssClass {
        border = theme.blockBorder
        backgroundColor = theme.blockBackgroundColor
    }

    val blockBorder by cssClass {
        border = theme.blockBorder
    }

    val justifySelfCenter by cssClass {
        justifySelf = "center"
    }

    val layout by cssClass {
        position = "absolute"
        top = 0
        left = 0
        width = "100%"
        height = "100%"
        overflow = "hidden"
    }

}