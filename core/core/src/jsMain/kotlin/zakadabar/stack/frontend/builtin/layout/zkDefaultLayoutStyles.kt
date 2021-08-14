/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.frontend.builtin.layout

import zakadabar.core.frontend.resources.css.*

val zkDefaultLayoutStyles by cssStyleSheet(ZkDefaultLayoutStyles())

open class ZkDefaultLayoutStyles : ZkCssStyleSheet() {

    open var appTitleBarHeight = "44px"

    open val defaultLayoutSmall by cssClass {
        + Display.flex
        + FlexDirection.column
        
        height = 100.percent
        width = 100.percent

        + Overflow.hidden
    }

    open val defaultLayoutLarge by cssClass {
        + Display.grid
        gridTemplateColumns = "max-content 1fr"
        gridTemplateRows = "$appTitleBarHeight 1fr"
        height = 100.percent
        width = 100.percent
        + Overflow.hidden
    }

    open val sideBarContainer by cssClass {
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