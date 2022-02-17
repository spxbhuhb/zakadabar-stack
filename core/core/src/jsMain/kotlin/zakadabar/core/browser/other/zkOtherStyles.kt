/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.other

import zakadabar.core.resource.ZkColors
import zakadabar.core.resource.css.*
import zakadabar.core.util.alpha

var zkOtherStyles by cssStyleSheet(ZkOtherStyles())

open class ZkOtherStyles : ZkCssStyleSheet() {

    open val chipsContainer by cssClass {
        borderRadius = "24px"
        backgroundColor = theme.primaryColor
        + Display.flex
        + AlignItems.center
        paddingLeft = "20px"
    }

    open val chipsLetters by cssClass {
        fontWeight = "400"
        color = ZkColors.white
        padding = "3px"
    }

    open val profContainer by cssClass {
        backgroundColor = ZkColors.white.alpha(0.5)
        borderRadius = "50%"
        width = "42px"
        height = "36px"
    }

    open val profLetters by cssClass {
        padding = "4px"
        fontWeight = "600"
        color = theme.primaryColor
        fontSize = "150%"
        textAlign = "center"
    }
}