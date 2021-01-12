/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.table

import zakadabar.stack.frontend.application.Application
import zakadabar.stack.frontend.resources.ZkTheme
import zakadabar.stack.frontend.util.CssStyleSheet

class ZkTableStyles(theme: ZkTheme) : CssStyleSheet<ZkTableStyles>(theme) {

    companion object {
        val tableClasses = ZkTableStyles(Application.theme).attach()
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
            background = "#6c7ae0"
            textAlign = "left"
            color = "white"
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
            color = "#505050"
            fontWeight = theme.fontWeight
        }

        on(" tr:nth-child(even) td") {
            background = "#f8f6ff"
        }
    }

}