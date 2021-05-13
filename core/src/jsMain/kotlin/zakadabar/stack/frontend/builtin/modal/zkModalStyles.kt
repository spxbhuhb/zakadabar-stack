/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.modal

import zakadabar.stack.frontend.resources.ZkTheme
import zakadabar.stack.frontend.resources.css.ZkCssStyleSheet
import zakadabar.stack.frontend.resources.css.cssStyleSheet

var zkModalStyles by cssStyleSheet(ZkModalStyles())

class ZkModalStyles : ZkCssStyleSheet() {

    val modalContainer by cssClass {
        position = "fixed"
        top = 0
        left = 0
        height = "100vh"
        width = "100vw"
        justifyContent = "center"
        alignItems = "center"
        display = "flex"
        backgroundColor = "rgba(0,0,0,0.5)"
        zIndex = 1900
    }

    val modal by cssClass {
        background = theme.backgroundColor
        border = theme.border
    }

    val title by cssClass {
        paddingLeft = theme.spacingStep
        paddingRight = theme.spacingStep
        borderBottom = theme.border
    }

    val content by cssClass {
        padding = theme.spacingStep
    }

    val buttons by cssClass {
        display = "flex"
        flexDirection = "row"
        justifyContent = "space-around"
        paddingTop = theme.spacingStep
        paddingBottom = theme.spacingStep
    }

}