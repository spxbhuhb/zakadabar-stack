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
        paddingTop = 4
        paddingBottom = 4
        paddingLeft = 10
        paddingRight = 10
        borderRadius = 4
    }

    val info by cssClass {
        backgroundColor = theme.infoBackground
        color = theme.infoText
    }

    val success by cssClass {
        backgroundColor = theme.successBackground
        color = theme.successText
    }

    val warning by cssClass {
        backgroundColor = theme.warningBackground
        color = theme.warningText

    }

    val error by cssClass {
        backgroundColor = theme.errorBackground
        color = theme.errorText
    }

    init {
        attach()
    }

}