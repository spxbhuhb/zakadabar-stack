/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.softui.browser.theme.styles

import zakadabar.core.resource.css.*

open class SuiLayoutStyles : ZkCssStyleSheet() {

    open var headerBackground by cssParameter { "radial-gradient(circle, rgba(38,83,152,1) 0%, rgba(38,83,152,1) 47%, rgba(5,36,55,1) 100%)" }
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
        gridTemplateRows = "max-content min-content 1fr"

        height = 100.percent
        width = 100.percent

        + Overflow.hidden
    }

    open val headerContainer by cssClass {
        + Display.flex
        + AlignItems.center
        width = 100.percent
        minHeight = 60.px
        marginBottom = if (headerSeparator) 0.px else 20.px
        background = headerBackground
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
    }

    open val popupSideBarContainer by cssClass {
        + Position.absolute
        + OverflowY.auto

        zIndex = 100.zIndex

        width = 100.percent
        maxHeight = 100.percent

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
        paddingLeft = 10.px
    }

}