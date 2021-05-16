/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.layout

import zakadabar.stack.frontend.resources.css.ZkCssStyleSheet
import zakadabar.stack.frontend.resources.css.cssStyleSheet

val zkLayoutStyles by cssStyleSheet(ZkLayoutStyles())

class ZkLayoutStyles : ZkCssStyleSheet() {

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

    /**
     * A switchable border. Use this when you want to give the user the possibility to
     * hide it. The idea behind this is that some people like to have things separated
     * by borders, some don't.
     */
    val border by cssClass {
        border = theme.border
    }

    /**
     * A fix border. Use this when you want the border, no matter what. This is different
     * than [border] because border is meant to switchable by the user.
     */
    val fixBorder by cssClass {
        border = theme.fixBorder
    }

    /**
     * A block with background color from the theme and a fix border around it.
     */
    val block by cssClass {
        border = theme.fixBorder
        backgroundColor = theme.blockBackgroundColor
    }

    val justifySelfCenter by cssClass {
        justifySelf = "center"
    }

    val p1 by cssClass {
        padding = theme.spacingStep
    }

    val pl1 by cssClass {
        paddingLeft = theme.spacingStep
    }

    val fs80 by cssClass {
        fontSize = "80%"
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