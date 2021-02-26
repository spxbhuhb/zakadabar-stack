/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.toast

import zakadabar.stack.frontend.application.Application
import zakadabar.stack.frontend.util.CssStyleSheet

object ZkToastStyles : CssStyleSheet<ZkToastStyles>(Application.theme) {

    val toastContainer by cssClass {
        position = "fixed"
        right = 0
        bottom = 20
        display = "flex"
        flexDirection = "column"
        zIndex = 2000
        justifyContent = "flex-end"
    }

    val toast by cssClass {
        display = "flex"
        flexDirection = "row"
        alignItems = "center"
        marginRight = 10
        marginBottom = 10
        paddingTop = 8
        paddingBottom = 8
        paddingLeft = 10
        paddingRight = 10
        borderRadius = 4
        boxShadow = "0 2px 2px 0 rgba(0, 0, 0, 0.14), 0 3px 1px -2px rgba(0, 0, 0, 0.2), 0 1px 5px 0 rgba(0, 0, 0, 0.12)"
        borderRadius = 2
    }

    val info by cssClass {
        background = theme.infoBackground + " !important"
        color = theme.infoText + " !important"
        fill = theme.successText + " !important"
    }

    val success by cssClass {
        background = theme.successBackground + " !important"
        color = theme.successText + " !important"
        fill = theme.successText + " !important"
    }

    val warning by cssClass {
        background = theme.warningBackground + " !important"
        color = theme.warningText + " !important"
        fill = theme.warningText + " !important"
    }

    val error by cssClass {
        background = theme.errorBackground + " !important"
        color = theme.errorText + " !important"
        fill = theme.errorText + " !important"
    }

    init {
        attach()
    }

}