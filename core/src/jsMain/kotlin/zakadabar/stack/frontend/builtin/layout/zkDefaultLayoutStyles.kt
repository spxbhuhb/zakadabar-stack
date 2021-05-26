/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.layout

import zakadabar.stack.frontend.resources.css.ZkCssStyleSheet
import zakadabar.stack.frontend.resources.css.cssStyleSheet

val zkDefaultLayoutStyles by cssStyleSheet(ZkDefaultLayoutStyles())

open class ZkDefaultLayoutStyles : ZkCssStyleSheet() {

    open var appTitleBarHeight = "44px"

    open val defaultLayoutSmall by cssClass {
        display = "flex"
        flexDirection = "column"
        height = "100%"
        width = "100%"
        overflow = "hidden"
    }

    open val defaultLayoutLarge by cssClass {
        display = "grid"
        gridTemplateColumns = "max-content 1fr"
        gridTemplateRows = "$appTitleBarHeight 1fr"
        height = "100%"
        width = "100%"
        overflow = "hidden"
    }

    open val sideBarContainer by cssClass {
        minWidth = 200
        maxHeight = "100%"
        overflowY = "auto"
        borderRight = theme.border

        on(":not(:hover)") {
            styles["scrollbar-width"] = "none"
        }

        on(":hover") {
            styles["scrollbar-width"] = "4px"
        }

        on(":not(:hover)::-webkit-scrollbar") {
            display = "none"
        }

        on(":hover::-webkit-scrollbar") {
            width = 4
        }
    }

    open val popupSideBarContainer by cssClass {
        background = theme.backgroundColor
        zIndex = 100
        position = "absolute"
        width = "100%"
    }

    open val contentContainer by cssClass {
        height = "100%"
        maxHeight = "100%"
        overflowY = "hidden"
    }

}