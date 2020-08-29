/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.desktop

import zakadabar.stack.frontend.FrontendContext.theme
import zakadabar.stack.frontend.util.CssStyleSheet
import zakadabar.stack.frontend.util.Theme

class DesktopClasses(theme: Theme) : CssStyleSheet<DesktopClasses>(theme) {

    companion object {
        val desktopClasses = DesktopClasses(theme).attach()
    }

    val desktop by cssClass {
        boxSizing = "border-box"

        width = "100%"
        height = "100%"

        display = "flex"
        flexDirection = "column"

        borderRadius = 2
    }

    val header by cssClass {
        boxSizing = "border-box"

        display = "flex"
        flexDirection = "row"

        height = 26
        alignItems = "center"
        backgroundColor = it.headerBackground
    }

    val headerIcon by cssClass {
        boxSizing = "border-box"
        backgroundColor = it.headerForeground
        padding = 6
        height = 26
        width = 26
        fill = it.lightestColor
    }

    val center by cssClass {
        flexGrow = 1
        display = "flex"
        flexDirection = "row"
    }

    val main by cssClass {
        flexGrow = 1
    }

    val footer by cssClass {
        display = "flex"
        flexDirection = "row"
        alignItems = "center"
        boxSizing = "border-box"
        height = 24
        fontFamily = it.fontFamily
        fontSize = it.fontSize
        borderRadius = it.borderRadius
        backgroundColor = it.lightGray
    }

    val copyright by cssClass {
        paddingLeft = 16
        color = it.darkestGray
    }

}