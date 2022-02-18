/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.other

import zakadabar.core.resource.ZkColors
import zakadabar.core.resource.css.*
import zakadabar.core.util.alpha

var zkOtherStyles by cssStyleSheet(ZkOtherStyles())

open class ZkOtherStyles : ZkCssStyleSheet() {

    open val chipContainerWithOutButton by cssClass {
        + Display.flex
        + AlignItems.center
        + JustifyContent.center

        width = "fit-content"
        height = 32.px

        borderRadius = 24.px
        backgroundColor = theme.primaryColor
        paddingLeft = 16.px
        paddingRight = 16.px
    }

    open val chipContainerWithButton by cssClass {
        + Display.flex
        + AlignItems.center
        + JustifyContent.center

        width = "fit-content"
        height = 32.px

        borderRadius = 24.px
        backgroundColor = theme.primaryColor
        paddingLeft = 16.px
        paddingRight = 16.px
    }

    open val chipLetters by cssClass {
        fontWeight = "400"
        color = ZkColors.white
        padding = 3.px
    }

    open val profContainer by cssClass {
        backgroundColor = ZkColors.white.alpha(0.5)
        borderRadius = 50.percent
        width = 42.px
        height = 36.px
    }

    open val profLetters by cssClass {
        padding = 4.px
        fontWeight = "600"
        color = theme.primaryColor
        fontSize = 150.percent
        textAlign = "center"
    }
}