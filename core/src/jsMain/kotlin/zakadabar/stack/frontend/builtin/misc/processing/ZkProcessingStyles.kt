/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.misc.processing

import zakadabar.stack.frontend.application.ZkApplication
import zakadabar.stack.frontend.resources.ZkColors
import zakadabar.stack.frontend.resources.css.ZkCssStyleSheet

object ZkProcessingStyles : ZkCssStyleSheet<ZkProcessingStyles>(ZkApplication.theme) {

    val asButton by cssClass {
        fontSize = 14
        color = theme.button.text
        backgroundColor = ZkColors.Gray.c600
        paddingTop = 6
        paddingBottom = 6
        paddingLeft = 10
        paddingRight = 10
        borderWidth = 1
        borderRadius = 2
    }

    init {
        attach()
    }
}