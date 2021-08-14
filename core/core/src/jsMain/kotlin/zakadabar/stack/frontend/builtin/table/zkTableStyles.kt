/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.frontend.builtin.table

import zakadabar.core.frontend.resources.css.*

val zkTableStyles by cssStyleSheet(ZkTableStyles())

open class ZkTableStyles : ZkCssStyleSheet() {

    open var tableBackgroundColor by cssParameter { theme.backgroundColor }
    open var headerBackground by cssParameter { theme.backgroundColor }
    open var headerText by cssParameter { theme.textColor }
    open var oddRowBackground by cssParameter { theme.backgroundColor }
    open var textColor by cssParameter { theme.textColor }
    open var hoverBackgroundColor by cssParameter { theme.hoverBackgroundColor }
    open var hoverTextColor by cssParameter { theme.hoverTextColor }
    open var rowBorderColor by cssParameter { theme.borderColor }
    open var headerBottomBorder by cssParameter { theme.fixBorder }
    open var border by cssParameter<String?> { null }
    open var actionTextColor by cssParameter { theme.primaryColor }
    open var controlColor by cssParameter { theme.primaryColor }

    val outerContainer by cssClass {
        + Display.flex
        + FlexDirection.column
        width = 100.percent
        height = 100.percent
    }

    val contentContainer by cssClass {
        + Position.relative

        flexGrow = 1.0

        + Overflow.auto

        backgroundColor = theme.backgroundColor
        boxShadow = theme.boxShadow
        borderRadius = 2.px
    }

    val resizeHandle by cssClass {
        + BoxSizing.borderBox
        + Position.absolute
        top = 0.px
        right = 0.px
        bottom = 0.px
        borderRight = "1px solid $controlColor"
        borderLeft = "1px solid $controlColor"
        backgroundColor = headerBackground
        marginTop = 4.px
        marginBottom = 4.px
        marginRight = 8.px
        opacity = 0.opacity
        width = 5.px
        cursor = "col-resize"

        on(":hover") {
            opacity = 1.opacity
        }
    }

    val beingResized by cssClass {
        on(" .$resizeHandle") {
            opacity = 1.opacity
        }
    }

    val noSelect by cssClass {
        + UserSelect.none

        styles["-moz-user-select"] = "none"
        styles["-webkit-user-select"] = "none"
        styles["-ms-user-select"] = "none"
    }

    val sortSign by cssClass {
        + BoxSizing.borderBox
        + Position.absolute
        top = 0.px
        right = 10.px
        bottom = 0.px
    }

    val sortedDescending by cssClass {
        marginTop = 16.px
        marginRight = 12.px
        width = 0.px
        height = 0.px
        borderLeft = "6px solid transparent"
        borderRight = "6px solid transparent"
        borderTop = "6px solid $controlColor"
    }

    val sortedAscending by cssClass {
        marginTop = 16.px
        marginRight = 12.px
        width = 0.px
        height = 0.px
        borderLeft = "6px solid transparent"
        borderRight = "6px solid transparent"
        borderBottom = "6px solid $controlColor"
    }

    val table by cssClass {
        + BoxSizing.borderBox
        + Display.grid

        borderCollapse = "collapse"
        minWidth = 100.percent

        backgroundColor = tableBackgroundColor

        on(" thead") {
            + Display.contents
        }

        on(" tbody") {
            + Display.contents
        }

        on(" tr") {
            + Display.contents
            + Cursor.pointer
        }

        on(" tr:hover td") {
            backgroundColor = hoverBackgroundColor
            color = hoverTextColor
        }

        on(" th") {

            + Position.sticky
            + Overflow.hidden
            + WhiteSpace.nowrap
            + TextAlign.left

            + Cursor.pointer

            paddingTop = 10.px
            paddingBottom = 10.px
            paddingRight = 8.px
            textOverflow = "ellipsis"
            textTransform = "uppercase"
            fontSize = 75.percent
            fontWeight = 400.weight
            top = 0.px
            background = headerBackground

            color = headerText
            borderBottom = headerBottomBorder
            zIndex = 30.zIndex
        }

        on(" th:hover .$resizeHandle") {
            opacity = 1.opacity
        }

        on(" th:first-child") {
            paddingLeft = 10.px
        }

        on(" th:last-child") {

        }

        on(" td") {

            + Overflow.hidden
            + WhiteSpace.nowrap

            zIndex = 20.zIndex

            paddingTop = 10.px
            paddingBottom = 10.px
            textOverflow = "ellipsis"
            color = textColor
            borderBottom = "1px solid $rowBorderColor"
            backgroundColor = oddRowBackground
        }

        if (this@ZkTableStyles.border != null) {

            on(" tr td:first-child") {
                borderLeft = this@ZkTableStyles.border
            }

            on(" tr td:last-child") {
                borderRight = this@ZkTableStyles.border
            }

            on(" tr:last-child td") {
                borderBottom = this@ZkTableStyles.border
            }
        }

//        on(" tr:nth-child(even) td") {
//            background = evenRowBackground
//        }

        on(" td:first-child") {
            paddingLeft = 10.px
        }

        on(" tr:last-child td:first-child") {
            if (this@ZkTableStyles.border != null) {
                borderBottomLeftRadius = 4.px
            }
        }

        on(" tr:last-child td:last-child") {
            if (this@ZkTableStyles.border != null) {
                borderBottomRightRadius = 4.px
            }
        }
    }

    val dense by cssClass {
        padding = "0 !important"
    }

    val action by cssClass {
        + Display.flex
        + AlignItems.center

        + WhiteSpace.nowrap
        + Cursor.pointer

        textTransform = "uppercase"
        fontSize = "75%"
        lineHeight = "1.3em"
        fontWeight = "400 !important"
        color = "$actionTextColor !important"
    }

}