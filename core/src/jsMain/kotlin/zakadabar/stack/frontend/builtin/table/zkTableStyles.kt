/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.table

import zakadabar.stack.frontend.resources.css.ZkCssStyleSheet
import zakadabar.stack.frontend.resources.css.cssParameter
import zakadabar.stack.frontend.resources.css.cssStyleSheet

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
        display = "flex"
        flexDirection = "column"
        width = "100%"
        height = "100%"
    }

    val contentContainer by cssClass {
        position = "relative"
        backgroundColor = theme.backgroundColor
        flexGrow = 1
        overflow = "auto"
        boxShadow = theme.boxShadow
        borderRadius = 2
    }

    val resizeHandle by cssClass {
        boxSizing = "border-box"
        position = "absolute"
        top = 0
        right = 0
        bottom = 0
        borderRight = "1px solid $controlColor"
        borderLeft = "1px solid $controlColor"
        backgroundColor = headerBackground
        marginTop = 4
        marginBottom = 4
        marginRight = 8
        opacity = 0
        width = 5
        cursor = "col-resize"

        on(":hover") {
            opacity = 1
        }
    }

    val beingResized by cssClass {
        on(" .$resizeHandle") {
            opacity = 1
        }
    }

    val noSelect by cssClass {
        userSelect = "none"
        styles["-moz-user-select"] = "none"
        styles["-webkit-user-select"] = "none"
        styles["-ms-user-select"] = "none"
    }

    val sortSign by cssClass {
        boxSizing = "border-box"
        position = "absolute"
        top = 0
        right = 10
        bottom = 0
    }

    val sortedDescending by cssClass {
        marginTop = 16
        marginRight = 12
        width = 0
        height = 0
        borderLeft = "6px solid transparent"
        borderRight = "6px solid transparent"
        borderTop = "6px solid $controlColor"
    }

    val sortedAscending by cssClass {
        marginTop = 16
        marginRight = 12
        width = 0
        height = 0
        borderLeft = "6px solid transparent"
        borderRight = "6px solid transparent"
        borderBottom = "6px solid $controlColor"
    }

    val table by cssClass {
        boxSizing = "border-box"

        display = "grid"
        borderCollapse = "collapse"
        minWidth = "100%"

        backgroundColor = tableBackgroundColor

        on(" thead") {
            display = "contents"
        }

        on(" tbody") {
            display = "contents"
        }

        on(" tr") {
            display = "contents"
        }

        on(" tr:hover td") {
            backgroundColor = hoverBackgroundColor
            color = hoverTextColor
        }

        on(" th") {
            paddingTop = 10
            paddingBottom = 10
            paddingRight = 8
            overflow = "hidden"
            textOverflow = "ellipsis"
            textTransform = "uppercase"
            fontSize = "75%"
            fontWeight = 400
            whiteSpace = "nowrap"
            position = "sticky"
            top = 0
            background = headerBackground
            textAlign = "left"
            color = headerText
            borderBottom = headerBottomBorder
            cursor = "pointer"
            zIndex = 30
        }

        on(" th:hover .$resizeHandle") {
            opacity = 1
        }

        on(" th:first-child") {
            paddingLeft = 10
        }

        on(" th:last-child") {

        }

        on(" td") {
            paddingTop = 10
            paddingBottom = 10
            overflow = "hidden"
            textOverflow = "ellipsis"
            whiteSpace = "nowrap"
            color = textColor
            borderBottom = "1px solid $rowBorderColor"
            backgroundColor = oddRowBackground
            zIndex = 20
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
            paddingLeft = 10
        }

        on(" tr:last-child td:first-child") {
            borderBottomLeftRadius = 4
        }

        on(" tr:last-child td:last-child") {
            borderBottomRightRadius = 4
        }
    }

    val action by cssClass {
        display = "flex"
        alignItems = "center"
        textTransform = "uppercase"
        fontSize = "75%"
        lineHeight = "1.3em"
        fontWeight = "400 !important"
        whiteSpace = "nowrap"
        cursor = "pointer"
        color = "$actionTextColor !important"
    }

}