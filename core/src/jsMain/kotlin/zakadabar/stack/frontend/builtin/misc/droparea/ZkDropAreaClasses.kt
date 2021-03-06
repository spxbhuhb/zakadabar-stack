/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.misc.droparea

import zakadabar.stack.frontend.application.Application
import zakadabar.stack.frontend.resources.MaterialColors
import zakadabar.stack.frontend.resources.ZkTheme
import zakadabar.stack.frontend.util.CssStyleSheet

class ZkDropAreaClasses(theme: ZkTheme) : CssStyleSheet<ZkDropAreaClasses>(theme) {

    companion object {
        var classes = ZkDropAreaClasses(Application.theme).attach()
    }

    val dropArea by cssClass {
        boxSizing = "border-box"
        flexGrow = 1
        width = "100%"
        height = "100%"
        display = "flex"
        flexDirection = "row"
        justifyContent = "center"
        alignItems = "center"
        color = theme.darkestGray
        fill = theme.darkestGray

        padding = 20

        backgroundColor = "#f5f5f5"
        borderRadius = 2
        border = "1px dotted lightgray"

        on(":hover") {
            backgroundColor = MaterialColors.LightBlue.c50
        }
    }

    val dropAreaMessage by cssClass {
        fontFamily = theme.fontFamily
        fontSize = 14
        fontWeight = 400
        paddingLeft = 6
    }

}