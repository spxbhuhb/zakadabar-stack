/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.examples.frontend.themes

import zakadabar.stack.frontend.resources.ZkColors
import zakadabar.stack.frontend.resources.ZkTheme
import zakadabar.stack.frontend.resources.css.ZkCssStyleSheet

class DemoRedStyles : ZkCssStyleSheet<ZkTheme>() {

    val exampleStyle by cssClass {
        color = ZkColors.white
        padding = 20
        backgroundColor = ZkColors.Red.c600
    }

}