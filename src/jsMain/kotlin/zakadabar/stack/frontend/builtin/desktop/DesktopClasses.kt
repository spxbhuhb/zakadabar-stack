/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.desktop

import zakadabar.stack.frontend.FrontendContext.theme
import zakadabar.stack.frontend.builtin.util.header.HeaderClasses
import zakadabar.stack.frontend.util.Theme

open class DesktopClasses(theme: Theme) : HeaderClasses(theme) {

    companion object {
        val desktopClasses = DesktopClasses(theme).attach() as DesktopClasses
    }

    val desktop by cssClass {
        boxSizing = "border-box"

        width = "100%"
        height = "100%"

        display = "flex"
        flexDirection = "column"

        borderRadius = 2
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
        fontFamily = theme.fontFamily
        fontSize = theme.fontSize
        borderRadius = theme.borderRadius
        backgroundColor = theme.lightGray
    }

    val copyright by cssClass {
        paddingLeft = 16
        color = theme.darkestGray
    }

}