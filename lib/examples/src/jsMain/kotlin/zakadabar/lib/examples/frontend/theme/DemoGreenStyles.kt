/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.examples.frontend.theme

import zakadabar.core.resource.ZkColors
import zakadabar.core.resource.css.ZkCssStyleSheet
import zakadabar.core.resource.css.px

class DemoGreenStyles : ZkCssStyleSheet() {

    val exampleStyle by cssClass {
        color = ZkColors.white
        padding = 20.px
        backgroundColor = ZkColors.Green.c600

        hover {
            backgroundColor = ZkColors.Green.a400
        }
    }

}