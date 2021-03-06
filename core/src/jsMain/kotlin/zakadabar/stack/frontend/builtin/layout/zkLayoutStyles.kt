/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.layout

import zakadabar.stack.frontend.resources.css.*

val zkLayoutStyles by cssStyleSheet(ZkLayoutStyles())

class ZkLayoutStyles : ZkCssStyleSheet() {

    val w100 by cssClass {
        width = 100.percent
    }

    val h100 by cssClass {
        height = 100.percent
    }

    val hidden by cssClass {
        display = "none !important"
    }

    val row by cssClass {
        + Display.flex
        + FlexDirection.row
    }

    val column by cssClass {
        + Display.flex
        + FlexDirection.column
    }

    val grow by cssClass {
        flexGrow = 1.0
    }

    val grid by cssClass {
        + Display.grid
        borderCollapse = "collapse"
        border = "none"
    }

    val gridGap by cssClass {
        gap = theme.spacingStep.px
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
     * Switch off the border with "!important".
     */
    val noBorder by cssClass {
        border = "none !important"
    }

    /**
     * A block with background color from the theme and a fix border around it.
     */
    val block by cssClass {
        border = theme.fixBorder
        backgroundColor = theme.blockBackgroundColor
    }

    val justifySelfCenter by cssClass {
        + JustifySelf.center
    }

    val alignSelfCenter by cssClass {
        + AlignSelf.center
    }

    val p1 by cssClass {
        padding = theme.spacingStep.px
    }

    val pl1 by cssClass {
        paddingLeft = theme.spacingStep.px
    }

    val fs80 by cssClass {
        fontSize = 80.percent
    }

    val layout by cssClass {
        + Position.absolute
        top = 0.px
        left = 0.px
        width = 100.percent
        height = 100.percent
        + Overflow.hidden
    }

}