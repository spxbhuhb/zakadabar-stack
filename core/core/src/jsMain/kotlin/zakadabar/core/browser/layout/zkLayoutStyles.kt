/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.layout

import zakadabar.core.resource.css.*

var zkLayoutStyles by cssStyleSheet(ZkLayoutStyles())

open class ZkLayoutStyles : ZkCssStyleSheet() {

    open val w100 by cssClass {
        width = 100.percent
    }

    open val h100 by cssClass {
        height = 100.percent
    }

    open val hidden by cssClass {
        display = "none !important"
    }

    open val row by cssClass {
        + Display.flex
        + FlexDirection.row
    }

    open val column by cssClass {
        + Display.flex
        + FlexDirection.column
    }

    open val grow by cssClass {
        flexGrow = 1.0
    }

    open val grid by cssClass {
        + Display.grid
        borderCollapse = "collapse"
        border = "none"
    }

    open val gridGap by cssClass {
        gap = theme.spacingStep.px
    }

    /**
     * A switchable border. Use this when you want to give the user the possibility to
     * hide it. The idea behind this is that some people like to have things separated
     * by borders, some don't.
     */
    open val border by cssClass {
        border = theme.border
    }

    /**
     * A fix border. Use this when you want the border, no matter what. This is different
     * than [border] because border is meant to switchable by the user.
     */
    open val fixBorder by cssClass {
        border = theme.fixBorder
    }

    /**
     * A rounded border.
     */
    val roundBorder by cssClass {
        borderRadius = theme.cornerRadius.px
    }

    /**
     * Switch off the border with "!important".
     */
    open val noBorder by cssClass {
        border = "none !important"
    }

    /**
     * A block with background color from the theme and a fix border around it.
     */
    open val block by cssClass {
        border = theme.fixBorder
        backgroundColor = theme.blockBackgroundColor
    }

    open val justifySelfCenter by cssClass {
        + JustifySelf.center
    }

    open val alignSelfCenter by cssClass {
        + AlignSelf.center
    }

    open val p1 by cssClass {
        padding = theme.spacingStep.px
    }

    open val pl1 by cssClass {
        paddingLeft = theme.spacingStep.px
    }

    open val fs80 by cssClass {
        fontSize = 80.percent
    }

    open val layout by cssClass {
        + Position.absolute
        top = 0.px
        left = 0.px
        width = 100.percent
        height = 100.percent
        + Overflow.hidden
    }

    open val grid1 by cssClass {
        + Position.relative
        + BoxSizing.borderBox
        + Display.grid
        gridTemplateRows = "1fr"
        gridGap = theme.spacingStep.px
        padding = theme.spacingStep.px
    }

    open val grid2 by cssClass {
        + Position.relative
        + BoxSizing.borderBox
        + Display.grid
        gridTemplateRows = "max-content 1fr"
        gridGap = theme.spacingStep.px
        padding = theme.spacingStep.px
    }

    open val grid3 by cssClass {
        + Position.relative
        + BoxSizing.borderBox
        + Display.grid
        gridTemplateRows = "max-content 1fr max-content"
        gridGap = theme.spacingStep.px
        padding = theme.spacingStep.px
    }
}