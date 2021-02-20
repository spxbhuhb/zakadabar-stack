/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.table

import zakadabar.stack.frontend.application.Application
import zakadabar.stack.frontend.util.CssStyleSheet

object ZkTableStyles : CssStyleSheet<ZkTableStyles>(Application.theme) {

    val contentContainer by cssClass {
        padding = 10
    }

    val table by cssClass {
        display = "grid"
        borderCollapse = "collapse"
        minWidth = "100%"

        on(" thead") {
            display = "contents"
        }

        on(" tbody") {
            display = "contents"
        }

        on(" tr") {
            display = "contents"
        }

        on(" th") {
            padding = 15
            paddingTop = 10
            paddingBottom = 10
            overflow = "hidden"
            textOverflow = "ellipsis"
            whiteSpace = "nowrap"
            position = "sticky"
            top = 0
            background = theme.table.headerBackground
            textAlign = "left"
            color = theme.table.headerText
            fontWeight = "normal"
        }

        on(" th:first-child") {
            borderTopLeftRadius = 4
        }

        on(" th:last-child") {
            border = 0
            borderTopRightRadius = 4
        }

        on(" td") {
            padding = 15
            paddingTop = 10
            paddingBottom = 10
            overflow = "hidden"
            textOverflow = "ellipsis"
            whiteSpace = "nowrap"
            color = theme.table.text
            background = theme.table.oddRowBackground
            fontWeight = theme.fontWeight
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

        on(" tr:nth-child(even) td") {
            background = theme.table.evenRowBackground
        }

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