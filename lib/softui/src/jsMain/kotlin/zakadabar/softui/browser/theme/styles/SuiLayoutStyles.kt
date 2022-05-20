/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.softui.browser.theme.styles

import zakadabar.core.resource.css.*
import zakadabar.softui.browser.theme.base.Colors
import zakadabar.softui.browser.theme.base.linearGradient

open class SuiLayoutStyles : ZkCssStyleSheet() {

    open var headerHeight by cssParameter { 80 }
    open var headerBackground by cssParameter { linearGradient(Colors.alertColors.info.state, Colors.alertColors.info.main) }
    open var headerSeparator by cssParameter { true }
    open var separatorImage by cssParameter { "linear-gradient(to right, rgba(255, 255, 255, 0), rgba(255, 255, 255, 0.5), rgba(255, 255, 255, 0))" }

    open val defaultLayoutSmall by cssClass {
        + Display.flex
        + FlexDirection.column
        
        height = 100.percent
        width = 100.percent

        padding = 0.px
        + Overflow.hidden
    }

    open val defaultLayoutLarge by cssClass {
        + Display.grid
        + BoxSizing.borderBox

        gridTemplateColumns = "max-content 20px 1fr"
        gridTemplateRows = "${headerHeight}px min-content 1fr"

        height = 100.percent
        width = 100.percent

        + Overflow.hidden
    }

    open val headerContainer by cssClass {
        + Display.flex
        + AlignItems.center
        width = 100.percent
        minHeight = headerHeight.px
        marginBottom = if (headerSeparator) 0.px else 20.px
        background = headerBackground

        medium {
            minHeight = (headerHeight/2).px
        }
    }

    open val separator by cssClass {
        height = if (headerSeparator) 1.px else 0.px
        marginBottom = if (headerSeparator) 20.px else 0.px
        backgroundImage = separatorImage
    }

    open val sideBarContainer by cssClass {
        marginLeft = 60.px
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

        top = (headerHeight/2).px
        width = 100.percent
        maxHeight = "calc(100% - ${headerHeight/2}px)"

        background = theme.backgroundColor

        styles["scrollbar-width"] = "none"
        on("::-webkit-scrollbar") {
            + Display.none
        }
    }

    open val contentContainerLarge by cssClass {
        height = 100.percent
        maxHeight = 100.percent
        + OverflowY.hidden
    }

    open val contentContainerSmall by cssClass {
        height = 100.percent
        maxHeight = 100.percent
        + OverflowY.hidden
        paddingLeft = 20.px
        paddingRight = 20.px
    }

}