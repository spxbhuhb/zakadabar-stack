/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.toast

import zakadabar.stack.frontend.resources.ZkTheme
import zakadabar.stack.frontend.resources.css.ZkCssStyleSheet

object ZkToastStyles : ZkCssStyleSheet<ZkTheme>() {

    val toastContainer by cssClass {
        position = "fixed"
        right = 0
        bottom = 20
        display = "flex"
        flexDirection = "column"
        zIndex = 2000
        justifyContent = "flex-end"
    }

    val toastContent by cssClass {
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
        background = theme.toast.infoBackground + " !important"
        color = theme.toast.infoText + " !important"
        fill = theme.toast.successText + " !important"
    }

    val success by cssClass {
        background = theme.toast.successBackground + " !important"
        color = theme.toast.successText + " !important"
        fill = theme.toast.successText + " !important"
    }

    val warning by cssClass {
        background = theme.toast.warningBackground + " !important"
        color = theme.toast.warningText + " !important"
        fill = theme.toast.warningText + " !important"
    }

    val error by cssClass {
        background = theme.toast.errorBackground + " !important"
        color = theme.toast.errorText + " !important"
        fill = theme.toast.errorText + " !important"
    }

    init {
        attach()
    }

}