/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.frontend.builtin.dock

import zakadabar.core.frontend.resources.ZkColors
import zakadabar.core.frontend.resources.css.*

val zkDockStyles by cssStyleSheet(ZkDockStyles())

open class ZkDockStyles : ZkCssStyleSheet() {

    open var dockBackground by cssParameter { ZkColors.white }
    open var headerBackground by cssParameter { ZkColors.BlueGray.c600 }
    open var headerForeground by cssParameter { ZkColors.black }
    open var headerIconBackground by cssParameter { "transparent" }
    open var headerIconFill by cssParameter { ZkColors.white }
    open var headerHeight by cssParameter { 26 }

    val dock by cssClass {
        + Position.fixed
        + Display.flex
        + FlexDirection.row
        + JustifyContent.flexEnd

        right = 0.px
        bottom = 0.px
        width = 100.percent
        zIndex = 1000.zIndex
    }

    val dockItem by cssClass {
        + Display.flex
        + FlexDirection.column

        backgroundColor = dockBackground
    }

    val header by cssClass {
        + BoxSizing.borderBox

        + Display.flex
        + FlexDirection.row
        + AlignItems.center

        + Overflow.hidden

        minHeight = headerHeight.px
        height = headerHeight.px
        backgroundColor = headerBackground
    }

    val headerIcon by cssClass {

        + BoxSizing.borderBox

        backgroundColor = headerIconBackground
        fill = headerIconFill
        marginLeft = 8.px
        marginRight = 8.px
    }

    val text by cssClass {
        + Display.flex
        + FlexDirection.row
        + AlignItems.center

        color = headerForeground
        fontSize = theme.fontSize
        height = 21.px
    }

    val extensions by cssClass {
        + Display.flex
        + FlexDirection.row
        + JustifyContent.flexEnd
        + AlignItems.center
        flexGrow = 1.0
        paddingLeft = 8.px
    }

    val extensionIcon by cssClass {
        + BoxSizing.borderBox

        + Cursor.pointer
        + UserSelect.none

        backgroundColor = headerIconBackground
        fill = headerIconFill
        strokeWidth = 2.px
        marginRight = 12.px
    }
}