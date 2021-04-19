/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.modal

import zakadabar.stack.frontend.resources.ZkTheme
import zakadabar.stack.frontend.resources.css.ZkCssStyleSheet

object ZkModalStyles : ZkCssStyleSheet<ZkTheme>() {

    val modalContainer by cssClass {
        position = "fixed"
        top = 0
        left = 0
        height = "100vh"
        width = "100vw"
        justifyContent = "center"
        alignItems = "center"
        display = "flex"
        backgroundColor = "rgba(0,0,0,0.2)"
        zIndex = 1900
    }

    val modal by cssClass {
        background = theme.modal.background
        border = theme.modal.border
    }

    val title by cssClass {
        paddingLeft = theme.layout.paddingStep * 2
        borderBottom = "1px solid ${theme.modal.border} !important"
    }

    val content by cssClass {
        padding = theme.layout.paddingStep * 2
    }

    val buttons by cssClass {
        display = "flex"
        flexDirection = "row"
        justifyContent = "space-around"
        paddingTop = theme.layout.paddingStep
        paddingBottom = theme.layout.paddingStep * 2
    }

    init {
        attach()
    }

}