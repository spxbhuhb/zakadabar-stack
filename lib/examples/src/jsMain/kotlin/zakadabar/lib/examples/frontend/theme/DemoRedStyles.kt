/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.examples.frontend.theme

import zakadabar.stack.frontend.resources.ZkColors
import zakadabar.stack.frontend.resources.css.ZkCssStyleSheet
import zakadabar.stack.frontend.resources.css.px

class DemoRedStyles : ZkCssStyleSheet() {

    val exampleStyle by cssClass {
        color = ZkColors.white
        padding = 20.px
        backgroundColor = ZkColors.Red.c600
    }

}