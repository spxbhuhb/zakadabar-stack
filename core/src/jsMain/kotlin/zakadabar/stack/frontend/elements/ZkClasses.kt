/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.elements

import zakadabar.stack.frontend.application.Application
import zakadabar.stack.frontend.resources.ZkTheme
import zakadabar.stack.frontend.util.CssStyleSheet

/**
 * Basic CSS classes used by ZkElement and ZkBuilder.
 */
class ZkClasses(theme: ZkTheme) : CssStyleSheet<ZkClasses>(theme) {

    companion object {
        val zkClasses = ZkClasses(Application.theme).attach()
    }

    val w100 by cssClass {
        width = "100%"
    }

    val h100 by cssClass {
        height = "100%"
    }

    val hidden by cssClass {
        display = "none !important"
    }

    val row by cssClass {
        display = "flex"
        flexDirection = "row"
    }

    val column by cssClass {
        display = "flex"
        flexDirection = "column"
    }

    val grow by cssClass {
        flexGrow = 1
    }

    val grid by cssClass {
        display = "grid"
        borderCollapse = "collapse"
        border = 0
    }

    // TODO this should be in the application somewhere it think
    val titleBar by cssClass {
        minHeight = 44 // linked to ZkMenuStyles.title.height
        backgroundColor = "rgb(245,245,245)"
        borderBottom = "0.5px solid #ccc"
        display = "flex"
        flexDirection = "row"
        alignItems = "center"
        paddingLeft = 20
        fontSize = 16
    }

}