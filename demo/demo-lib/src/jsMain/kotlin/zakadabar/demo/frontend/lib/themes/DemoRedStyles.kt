/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.frontend.lib.themes

import zakadabar.stack.frontend.resources.ZkColors
import zakadabar.stack.frontend.resources.css.ZkCssStyleSheet

class DemoRedStyles : ZkCssStyleSheet() {

    val exampleStyle by cssClass {
        color = ZkColors.white
        padding = 20
        backgroundColor = ZkColors.Red.c600
    }

}