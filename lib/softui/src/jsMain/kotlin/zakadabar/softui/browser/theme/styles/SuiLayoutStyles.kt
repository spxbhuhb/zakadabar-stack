/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.softui.browser.theme.styles

import zakadabar.core.resource.css.*
import zakadabar.softui.browser.theme.base.Colors
import zakadabar.softui.browser.theme.base.linearGradient
open class SuiLayoutStyles : ZkCssStyleSheet() {

    open var largeHeaderHeight by cssParameter { 60 }
    open var mediumHeaderHeight by cssParameter { 40 }
    open var headerBottomMargin by cssParameter { 10 }
    open var sideBarLeftMargin by cssParameter { 20 }
    open var gridMiddleWidth by cssParameter { 0 }
    open var contentRightMargin by cssParameter { 0 }
    open var headerBackground by cssParameter { linearGradient(Colors.alertColors.info.state, Colors.alertColors.info.main) }
    open var headerSeparator by cssParameter { true }
    open var separatorImage by cssParameter { "linear-gradient(to right, rgba(255, 255, 255, 0), rgba(255, 255, 255, 0.5), rgba(255, 255, 255, 0))" }

    open fun ZkCssStyleRule.common() {
        + Display.grid
        + BoxSizing.borderBox
        + Overflow.hidden
        height = 100.percent
        width = 100.percent
    }

    open val defaultLayoutSmall by cssClass {
        common()
        gridTemplateColumns = "1fr"
        gridTemplateRows = "${mediumHeaderHeight}px min-content min-content 1fr"
    }

    open val defaultLayoutLarge by cssClass {
        common()
        gridTemplateColumns = "max-content ${gridMiddleWidth}px 1fr ${contentRightMargin.px}"
        gridTemplateRows = "${largeHeaderHeight}px min-content max-content 1fr"
    }

    open val headerContainer by cssClass {
        + Display.flex
        + AlignItems.center
        width = 100.percent
        minHeight = largeHeaderHeight.px
        marginBottom = if (headerSeparator) 0.px else headerBottomMargin.px
        background = headerBackground

        medium {
            minHeight = mediumHeaderHeight.px
        }
    }

    open val separator by cssClass {
        height = if (headerSeparator) 1.px else 0.px
        marginBottom = if (headerSeparator) headerBottomMargin.px else 0.px
        backgroundImage = separatorImage
    }

    open val sideBarContainer by cssClass {
        marginLeft = sideBarLeftMargin.px
        minWidth = 220.px
        maxHeight = 100.percent
        + OverflowY.auto
        borderRight = theme.border

        on(":not(:hover)") {
            styles["scrollbar-width"] = "none"
        }

        on(":hover") {
            styles["scrollbar-width"] = "4px"
        }

        on(":not(:hover)::-webkit-scrollbar") {
            + Display.none
        }

        on(":hover::-webkit-scrollbar") {
            width = 4.px
        }

        medium {
            marginLeft = 20.px
        }
    }

    open val popupSideBarContainer by cssClass {
        + Position.absolute
        + OverflowY.auto

        zIndex = 100.zIndex

        top = mediumHeaderHeight.px
        width = 100.percent
        maxHeight = "calc(100% - ${mediumHeaderHeight}px)"

        background = theme.backgroundColor

        styles["scrollbar-width"] = "none"
        on("::-webkit-scrollbar") {
            + Display.none
        }
    }

    open val contentContainerLarge by cssClass {
        + Position.relative
        height = 100.percent
        maxHeight = 100.percent
        + OverflowY.hidden
    }

    open val contentContainerSmall by cssClass {
        height = 100.percent
        maxHeight = 100.percent
        + OverflowY.hidden
    }

}