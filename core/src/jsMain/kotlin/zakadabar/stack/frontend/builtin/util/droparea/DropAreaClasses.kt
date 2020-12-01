/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.util.droparea

import zakadabar.stack.frontend.application.Application
import zakadabar.stack.frontend.util.CssStyleSheet
import zakadabar.stack.frontend.util.Theme

class DropAreaClasses(theme: Theme) : CssStyleSheet<DropAreaClasses>(theme) {

    companion object {
        var classes = DropAreaClasses(Application.theme).attach()
    }

    val dropArea by cssClass {
        flexGrow = 1
        width = "100%"
        display = "flex"
        flexDirection = "row"
        minHeight = 20
        justifyContent = "center"
        alignItems = "center"
        height = "100%"
        color = theme.darkestGray
        fill = theme.darkestGray
        marginTop = 20
        on(":hover") {
            backgroundColor = "#f5f5f5"
            borderRadius = 2
            border = "1px dotted lightgray"
        }
    }

    val dropAreaMessage by cssClass {
        fontFamily = theme.fontFamily
        fontSize = 14
        fontWeight = 400
        paddingLeft = 6
    }

}