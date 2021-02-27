/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.table

import zakadabar.stack.frontend.application.Application
import zakadabar.stack.frontend.resources.MaterialColors
import zakadabar.stack.frontend.util.CssStyleSheet

object ZkTableStyles : CssStyleSheet<ZkTableStyles>(Application.theme) {

    val outerContainer by cssClass {
        display = "flex"
        flexDirection = "column"
        width = "100%"
        height = "100%"
    }

    val contentContainer by cssClass {
        backgroundColor = "rgb(245,245,245)"
        flexGrow = 1
        overflow = "scroll"
    }

    val table by cssClass {
        boxSizing = "border-box"

        display = "grid"
        borderCollapse = "collapse"
        minWidth = "100%"

        backgroundColor = MaterialColors.white
        boxShadow = "0 2px 2px 0 rgba(0, 0, 0, 0.14), 0 3px 1px -2px rgba(0, 0, 0, 0.2), 0 1px 5px 0 rgba(0, 0, 0, 0.12)"
        borderRadius = 2

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
            backgroundColor = theme.table.hoverBackground
        }

        on(" th") {
            padding = 15
            paddingTop = 10
            paddingBottom = 10
            overflow = "hidden"
            textOverflow = "ellipsis"
            textTransform = "uppercase"
            fontSize = "75%"
            fontWeight = 400
            whiteSpace = "nowrap"
            position = "sticky"
            top = 0
            background = theme.table.headerBackground
            textAlign = "left"
            color = theme.table.headerText
            borderBottom = "1px solid ${theme.table.headerBottom}"
        }

        on(" th:first-child") {

        }

        on(" th:last-child") {

        }

        on(" td") {
            padding = 15
            paddingTop = 10
            paddingBottom = 10
            overflow = "hidden"
            textOverflow = "ellipsis"
            whiteSpace = "nowrap"
            color = theme.table.text
            fontWeight = theme.fontWeight
            borderBottom = "1px solid ${theme.table.innerBorder}"
        }

        if (theme.table.border != null) {
            val border = theme.table.border

            on(" tr td:first-child") {
                borderLeft = border
            }

            on(" tr td:last-child") {
                borderRight = border
            }

            on(" tr:last-child td") {
                borderBottom = border
            }
        }

//        on(" tr:nth-child(even) td") {
//            background = theme.table.evenRowBackground
//        }

        on(" tr:last-child td:first-child") {
            borderBottomLeftRadius = 4
        }

        on(" tr:last-child td:last-child") {
            borderBottomRightRadius = 4
        }
    }

    init {
        attach()
    }

}