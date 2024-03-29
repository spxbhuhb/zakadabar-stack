/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.bender.frontend

import zakadabar.core.resource.css.*

val benderStyles by cssStyleSheet(BenderStyles())

class BenderStyles : ZkCssStyleSheet() {

    val editor by cssClass {
        margin = "auto"
        maxWidth = 800.px
    }

    val header by cssClass {
        + AlignItems.baseLine
        marginBlockStart = 0.83.em
        marginBlockEnd = 0.40.em
    }

    val headerText by cssClass {
        fontSize = 1.5.em
        fontWeight = 500.weight
    }

    val headerLink by cssClass {
        color = theme.infoColor
        paddingLeft = 20.px
        fontWeight = 400.px
        fontSize = 15.px
    }

    val mediumInput by cssClass {
        width = 10.em
    }

    val largeInput by cssClass {
        width = 16.em
    }

    val extraLargeInput by cssClass {
        width = 24.em
    }

    val editorLabel by cssClass {
        + AlignSelf.center

        fontSize = 80.percent
        marginRight = 5.px
    }

    val constraintEditor by cssClass {
        + AlignSelf.center

        marginRight = 10.px
    }

    val lastGenerated by cssClass {
        + AlignSelf.center

        marginRight = 10.px
    }
}