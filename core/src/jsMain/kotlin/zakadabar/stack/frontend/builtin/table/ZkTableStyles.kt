/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.table

import zakadabar.stack.frontend.application.Application
import zakadabar.stack.frontend.util.CssStyleSheet

object ZkTableStyles : CssStyleSheet<ZkTableStyles>(Application.theme) {

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

        on(" th:last-child") {
            border = 0
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

        on(" tr:nth-child(even) td") {
            background = theme.table.evenRowBackground
        }
    }

    init {
        attach()
    }

}