/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.misc.account

import zakadabar.stack.frontend.application.Application
import zakadabar.stack.frontend.util.CssStyleSheet

object ZkAccountStyles : CssStyleSheet<ZkAccountStyles>(Application.theme) {

    val avatar by cssClass {
        backgroundColor = theme.darkColor
        width = 28
        height = 28
        fontSize = 14
        color = theme.lightestColor
        textAlign = "center"
        lineHeight = 28
        borderRadius = "50%"
    }

    init {
        attach()
    }
}